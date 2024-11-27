package com.example.epolisplusapp.ui.dopservice.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.epolisplusapp.R
import com.example.epolisplusapp.adapters.LitroCalculatorAdapter
import com.example.epolisplusapp.databinding.DopUslugiActivityBinding
import com.example.epolisplusapp.ui.dopservice.dialog.DopFormsDialog
import com.example.epolisplusapp.ui.dopservice.dialog.DopUslugiLitroDialog
import com.example.epolisplusapp.util.CommonUtils
import java.util.Locale

class DopUslugiActivity : AppCompatActivity() {

    private lateinit var dopuslugiAdapter: LitroCalculatorAdapter
    private lateinit var dopUslugiViewModel: DopUslugiActivityViewModel
    private lateinit var binding: DopUslugiActivityBinding
    private var items: Int? = null
    private var discountPercent: Int = 0
    private var discountLength: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DopUslugiActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dopUslugiViewModel = DopUslugiActivityViewModel.create(this)
        binding.rcDop.layoutManager = GridLayoutManager(this, 2)
        dopuslugiAdapter = LitroCalculatorAdapter(
            emptyList(), R.string.dop_uslugi_currency, this, discountPercent, discountLength,
            { itemCount, totalSum ->
                items = itemCount
//                dopUslugiViewModel.updateSums(itemCount, totalSum, discountPercent, discountLength, this)

                val discount =
                    if (itemCount >= discountLength) (totalSum * discountPercent / 100).toInt() else 0
                val finalTotal = totalSum - discount

                binding.tvTotalSum.text = CommonUtils.formatSumWithSeparatorAndCurrency(
                    totalSum,
                    Locale("ru", "Ru"),
                    this,
                    R.string.dop_uslugi_currency
                )
                binding.tvDiscount.text = CommonUtils.formatSumWithSeparatorAndCurrency(
                    discount,
                    Locale("ru", "Ru"),
                    this,
                    R.string.dop_uslugi_currency
                )
                binding.tvFinalSum.text = CommonUtils.formatSumWithSeparatorAndCurrency(
                    finalTotal,
                    Locale("ru", "Ru"),
                    this,
                    R.string.dop_uslugi_currency
                )
            },
            { selectedItem ->
                val bottomSheetFragment = DopUslugiLitroDialog.newInstance(
                    selectedItem.name,
                    selectedItem.icon,
                    selectedItem.price,
                    selectedItem.info
                )
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
        )
        binding.rcDop.adapter = dopuslugiAdapter
        dopUslugiViewModel.fetcLitroList()
        binding.btOformit.setOnClickListener {
            dopUslugiViewModel.checkItems(items)
        }
        setupObservers()
        setupUtils()
    }

    private fun setupUtils() {
        val rootView = findViewById<View>(android.R.id.content)
        CommonUtils.setupToolbar(binding.dopToolbar, this)
        CommonUtils.setupBlurView(this, binding.dopBar, rootView)
    }

    private fun setupObservers() {
        dopUslugiViewModel.isItemSelectedLiveData.observe(this) { isSelected ->
            if (isSelected) {
                Log.d("1234", "$items")
                val bottomSheet = DopFormsDialog()
                bottomSheet.show(supportFragmentManager, "myBottomSheet")
            } else {
                CommonUtils.showCustomToast(this, "Выберите элемент перед продолжением")
            }
        }
        dopUslugiViewModel.dopListLiveData.observe(this) { dopList ->
            dopuslugiAdapter.updateData(
                dopList,
                dopUslugiViewModel.discountDataLiveData.value?.first ?: 0,
                dopUslugiViewModel.discountDataLiveData.value?.second ?: 0
            )
        }
        dopUslugiViewModel.discountDataLiveData.observe(this) { (percent, length) ->
            binding.tvDopDiscount.text = percent.toString()
        }

        dopUslugiViewModel.discountTitleLiveData.observe(this) { title ->
            binding.tvDopPresent.text = title
        }

        dopUslugiViewModel.discountBodyLiveData.observe(this) { body ->
            binding.tvDopPresent2.text = body
        }

        dopUslugiViewModel.progressBarVisible.observe(this) { isVisible ->
            binding.dopBar.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        dopUslugiViewModel.errorMessageLiveData.observe(this) { errorMessage ->
            val message = errorMessage.getErrorMessage(this)
            CommonUtils.showCustomToast(this, message)
        }
    }
}
