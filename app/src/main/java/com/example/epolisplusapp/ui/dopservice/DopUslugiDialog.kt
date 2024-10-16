package com.example.epolisplusapp.ui.dopservice

import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.epolisplusapp.R
import com.example.epolisplusapp.util.CommonUtils
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Locale

class DopUslugiDialog : BottomSheetDialogFragment() {
    private val baseUrl = "https://epolisplus.uz/"

    companion object {
        fun newInstance(
            name: String,
            icon: String,
            price: Int,
            info: String
        ): DopUslugiDialog {
            val fragment = DopUslugiDialog()
            val args = Bundle().apply {
                putString("name", name)
                putString("icon", icon)
                putInt("price", price)
                putString("info", info)
            }
            fragment.arguments = args
            return fragment
        }
    }

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
            Locale("ru", "Ru"),
            requireContext(),
            R.string.dop_uslugi_currency
        )
        view.findViewById<TextView>(R.id.tvDopDialogTitle).text = name
        val image= "${baseUrl}${icon}"
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
        CommonUtils.setupBottomSheetBehavior(view, this)
        val toolbar: MaterialToolbar = view.findViewById(R.id.dopToolbar)
        CommonUtils.setupToolbarDialog(toolbar, this)
        val button  = view.findViewById<Button>(R.id.button2)
        button.setOnClickListener {
            val bottomSheet = DopFormsFrag()
            bottomSheet.show(parentFragmentManager, "myBottomSheet1")
            dismiss()

        }


    }
}
