package com.example.epolisplusapp.ui.dopservice.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.epolisplusapp.R
import com.example.epolisplusapp.adapters.LitroCalculatorAdapter
import com.example.epolisplusapp.databinding.DopUslugiActivityBinding
import com.example.epolisplusapp.ui.dopservice.DopFormsSharedViewModel
import com.example.epolisplusapp.ui.dopservice.dialog.DopFormsDialog
import com.example.epolisplusapp.ui.dopservice.dialog.DopUslugiLitroDialog
import com.example.epolisplusapp.util.CommonUtils
import java.util.Locale

class DopUslugiActivity : AppCompatActivity() {

    private lateinit var dopuslugiAdapter: LitroCalculatorAdapter
    private lateinit var dopUslugiViewModel: DopUslugiActivityViewModel
    private lateinit var binding: DopUslugiActivityBinding

    private val sharedViewModel: DopFormsSharedViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DopUslugiActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dopUslugiViewModel = DopUslugiActivityViewModel.create(this)
        binding.rcDop.layoutManager = GridLayoutManager(this, 2)
        dopuslugiAdapter = LitroCalculatorAdapter(
            emptyList(),
            dopUslugiViewModel,
            sharedViewModel,
            this
        ) { selectedItem ->
            val bottomSheetFragment = DopUslugiLitroDialog.newInstance(
                selectedItem.name,
                selectedItem.icon,
                selectedItem.id,
                selectedItem.price,
                selectedItem.info
            )
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
        binding.rcDop.adapter = dopuslugiAdapter
        dopUslugiViewModel.fetcLitroList()
        binding.btOformit.setOnClickListener {
            dopUslugiViewModel.checkItems()
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

        dopUslugiViewModel.itemSelectedLiveData.observe(this) { selectedItems ->
            if (selectedItems.isEmpty()) {
                binding.tvTotalSum.text = "0"
                binding.tvDiscount.text = "0"
                binding.tvFinalSum.text = "0"
            }
        }

        dopUslugiViewModel.finalTotal.observe(this){ finalTotal ->
            val discount = dopUslugiViewModel.discount.value ?:0
            val totalSum = dopUslugiViewModel.totalSum.value ?:0
//            Log.d("adapter", "Total sum: $totalSum, Discount: $discount, Final total: $finalTotal")
            CommonUtils.formatAndSetTexts(
                this,
                listOf(
                    binding.tvTotalSum to totalSum,
                    binding.tvDiscount to discount,
                    binding.tvFinalSum to finalTotal
                )
            )
        }
        dopUslugiViewModel.totalSum.observe(this) { totalSum ->
            Log.d("adapter", "Updated total sum: $totalSum")
        }
        dopUslugiViewModel.navigateBoolean.observe(this) { isSelected ->
            if (isSelected == true) {
//                Log.d("adapter", "Items for is isItemSelectedLiveData:$items")
//                sharedViewModel.updateSelectedItems(dopUslugiViewModel.selectedItemsLiveData.value ?: emptyList())
                val bottomSheet = DopFormsDialog()
                Log.d("adapter", "updateSelectedItems1 ${dopUslugiViewModel.selectedItemsLiveData.value}")

                bottomSheet.show(supportFragmentManager, "myBottomSheet")
            }
        }
        dopUslugiViewModel.dopListLiveData.observe(this) { dopList ->

            dopuslugiAdapter.updateData(dopList)
        }
        dopUslugiViewModel.selectedItemsLiveData.observe(this){


        }
        dopUslugiViewModel.discountDataLiveData.observe(this) { (percent) ->
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
