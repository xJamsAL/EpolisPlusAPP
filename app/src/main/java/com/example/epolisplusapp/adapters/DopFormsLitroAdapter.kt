package com.example.epolisplusapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.epolisplusapp.R
import com.example.epolisplusapp.models.dopuslugi.LitroCalculatorItems
import com.example.epolisplusapp.ui.dopservice.dialog.DopFormsDialog
import com.example.epolisplusapp.util.CommonUtils

class DopFormsLitroAdapter(private var dopItems: List<LitroCalculatorItems>,
                           private val context: Context
) :
    RecyclerView.Adapter<DopFormsLitroAdapter.DopFormsViewHolder>() {
    private val baseUrl = "https://epolisplus.uz/"
    inner class DopFormsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icons: ImageView = itemView.findViewById(R.id.ivLitroIcon)
        val name: TextView = itemView.findViewById(R.id.tvLitroName)
        val price: TextView = itemView.findViewById(R.id.tvLitroPrice)

        fun bind(dop: LitroCalculatorItems) {
            val formattedPrice = CommonUtils.formatSumWithSeparatorAndCurrency(dop.price, context)
            name.text = dop.name
            price.text = formattedPrice
            val image = "${baseUrl}${dop.icon}"
            if (image.endsWith(".svg")) {
                Glide.with(itemView.context)
                    .`as`(PictureDrawable::class.java)
                    .load(image)
                    .error(R.drawable.auth_input_crossed_eye)
                    .into(icons)
            } else {
                Glide.with(itemView.context)
                    .load(image)
                    .error(R.drawable.auth_input_crossed_eye)
                    .into(icons)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DopFormsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dop_uslugi_dialog_litro_item_, parent, false)
        return DopFormsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DopFormsViewHolder, position: Int) {
        val dopUslug = dopItems[position]
        holder.bind(dopUslug)
    }

    override fun getItemCount() = dopItems.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<LitroCalculatorItems>) {
        Log.d("adapter", "Updating adapter data with: $newItems")
        dopItems = newItems
        notifyDataSetChanged()
        Log.d("adapter", "Adapter data updated.")
    }
}