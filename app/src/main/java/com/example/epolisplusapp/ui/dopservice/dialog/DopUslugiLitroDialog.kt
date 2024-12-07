package com.example.epolisplusapp.ui.dopservice.dialog

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.epolisplusapp.R
import com.example.epolisplusapp.models.dopuslugi.LitroCalculatorItems
import com.example.epolisplusapp.ui.dopservice.DopFormsSharedViewModel
import com.example.epolisplusapp.util.CommonUtils
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DopUslugiLitroDialog : BottomSheetDialogFragment() {
    private val baseUrl = "https://epolisplus.uz/"
    private lateinit var sharedViewModel: DopFormsSharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dop_uslugi_dialog, container, false)
        val name = arguments?.getString("name")
        val icon = arguments?.getString("icon")
        val price = arguments?.getInt("price")
        val info = arguments?.getString("info")
        val formattedPrice = CommonUtils.formatSumWithSeparatorAndCurrency(
            price ?: 0,
            requireContext()
        )
        view.findViewById<TextView>(R.id.tvDopDialogTitle).text = name
        val image = "${baseUrl}${icon}"
        view.findViewById<ImageView>(R.id.imageView33).let { imageView ->
            Glide.with(this).`as`(PictureDrawable::class.java).load(image)
                .error(R.drawable.auth_input_crossed_eye).into(imageView)
        }
        view.findViewById<TextView>(R.id.tvPrice).text = formattedPrice
        view.findViewById<TextView>(R.id.tvInfoDialog).text = info

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[DopFormsSharedViewModel::class.java]
        CommonUtils.setupBottomSheetBehavior(view, this)
        val toolbar: MaterialToolbar = view.findViewById(R.id.dopToolbar)
        CommonUtils.setupToolbarDialog(toolbar, this)
        val button = view.findViewById<Button>(R.id.button2)
        button.setOnClickListener {
            val item = LitroCalculatorItems(
                icon = arguments?.getString("icon") ?: "",
                id = arguments?.getInt("id") ?: 0,
                info = arguments?.getString("info") ?: "",
                name = arguments?.getString("name") ?: "",
                price = arguments?.getInt("price") ?: 0

            )
            sharedViewModel.updateSelectedItems(listOf(item))
            sharedViewModel.setFinalTotal(item.price)
            sharedViewModel.setDiscount(0)
            Log.d("1234", "item = $item")

            val bottomSheet = DopFormsDialog()
            bottomSheet.show(parentFragmentManager, "myBottomSheet1")
            dismiss()
        }
    }

    companion object {
        fun newInstance(
            name: String,
            icon: String,
            id: Int,
            price: Int,
            info: String
        ): DopUslugiLitroDialog {
            val fragment = DopUslugiLitroDialog()
            val args = Bundle().apply {
                putString("name", name)
                putString("icon", icon)
                putInt("id", id)
                putInt("price", price)
                putString("info", info)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
