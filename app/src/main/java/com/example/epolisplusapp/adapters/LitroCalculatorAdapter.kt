package com.example.epolisplusapp.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.epolisplusapp.R
import com.example.epolisplusapp.models.dopuslugi.LitroCalculatorItems
import com.example.epolisplusapp.util.CommonUtils
import java.util.Locale

class LitroCalculatorAdapter(
    private var dopItems: List<LitroCalculatorItems>,
    private val currencyResId: Int,
    private val context: Context,
    private var discountPercent: Int,
    private var discountLength: Int,
    private val onSelectionChanged: (Int, Int) -> Unit,
    private val onItemClick: (LitroCalculatorItems) -> Unit
) : RecyclerView.Adapter<LitroCalculatorAdapter.DopUslugiViewHolder>() {


    private val baseUrl = "https://epolisplus.uz/"
    private val selectedItems = mutableSetOf<Int>()
    private var totalSum = 0

    inner class DopUslugiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.ivDopItem)
        val name: TextView = itemView.findViewById(R.id.tvDopItem)
        val price: TextView = itemView.findViewById(R.id.tvDopItemPrice)

        init {
            val cardView: CardView = itemView.findViewById(R.id.cardDopItem)
            itemView.setOnClickListener {
                val position = adapterPosition
                val itemPrice = dopItems[position].price.toString().toInt()
                if (selectedItems.contains(position)) {
                    selectedItems.remove(position)
                    cardView.cardElevation = 4f
                    totalSum -= itemPrice
                } else {
                    selectedItems.add(position)
                    cardView.cardElevation = 5f
                    cardView.setBackgroundColor(Color.parseColor("#d6f0e8"))
                    totalSum += itemPrice
                }
                notifyItemChanged(position)
                onSelectionChanged(selectedItems.size, totalSum)
            }
        }

        fun bind(dop: LitroCalculatorItems) {
            val cardView: CardView = itemView.findViewById(R.id.cardDopItem)
            val formated =CommonUtils.formatSumWithSeparatorAndCurrency(
                dop.price, Locale.getDefault(), context, currencyResId
            )
            name.text = dop.name
            price.text = formated
            val image = "${baseUrl}${dop.icon}"
            if (image.endsWith(".svg")) {
                Glide.with(itemView.context)
                    .`as`(PictureDrawable::class.java)
                    .load(image)
                    .error(R.drawable.auth_input_crossed_eye)
                    .into(icon)
            } else {
                Glide.with(itemView.context)
                    .load(image)
                    .error(R.drawable.auth_input_crossed_eye)
                    .into(icon)
            }

            if (selectedItems.contains(adapterPosition)) {
                cardView.setCardBackgroundColor(Color.parseColor("#d6f0e8"))
                itemView.setBackgroundResource(R.drawable.dop_uslugi_selected_dop_item)
            } else {
                itemView.setBackgroundResource(R.drawable.dop_uslugi_unselected_item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DopUslugiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dop_uslugi_item, parent, false)
        return DopUslugiViewHolder(view)
    }

    override fun onBindViewHolder(holder: DopUslugiViewHolder, position: Int) {
        val dopuslugi = dopItems[position]
        holder.itemView.findViewById<ImageView>(R.id.ivInfoDop).setOnClickListener {
            onItemClick(dopuslugi)
        }
        holder.bind(dopuslugi)
    }

    override fun getItemCount() = dopItems.size

    fun updateData(newDopList: List<LitroCalculatorItems>, newDiscountPercent: Int, newDiscountLength: Int) {
        dopItems = newDopList
        discountPercent = newDiscountPercent
        discountLength = newDiscountLength
        notifyDataSetChanged()
    }
}
