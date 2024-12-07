package com.example.epolisplusapp.adapters

import android.annotation.SuppressLint
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
import com.example.epolisplusapp.ui.dopservice.DopFormsSharedViewModel
import com.example.epolisplusapp.ui.dopservice.activity.DopUslugiActivityViewModel
import com.example.epolisplusapp.util.CommonUtils

class LitroCalculatorAdapter(
    private var dopItems: List<LitroCalculatorItems>,
    private val viewModel: DopUslugiActivityViewModel,
    private var sharedViewModel: DopFormsSharedViewModel,
    private val context: Context,
    private val onItemClick: (LitroCalculatorItems) -> Unit
) : RecyclerView.Adapter<LitroCalculatorAdapter.DopUslugiViewHolder>() {

    private val baseUrl = "https://epolisplus.uz/"

    inner class DopUslugiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.ivDopItem)
        val name: TextView = itemView.findViewById(R.id.tvDopItem)
        val price: TextView = itemView.findViewById(R.id.tvDopItemPrice)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val itemPrice = dopItems[position].price
                    viewModel.toggleItemSelection(position, itemPrice)
                    val selectedItems = dopItems.filterIndexed { index, _ ->
                        viewModel.itemSelectedLiveData.value?.contains(index) == true
                    }

                    sharedViewModel.updateSelectedItems(selectedItems)

                    notifyItemChanged(position)
                }
            }
        }

        fun bind(dop: LitroCalculatorItems) {
            val cardView: CardView = itemView.findViewById(R.id.cardDopItem)
            val formattedPrice = CommonUtils.formatSumWithSeparatorAndCurrency(dop.price, context)
            name.text = dop.name
            price.text = formattedPrice

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

            val isSelected = viewModel.itemSelectedLiveData.value?.contains(adapterPosition) == true
//            Log.d("adapter", "Item ${dop.name} (Position: $adapterPosition) selected: $isSelected")
            if (isSelected) {
//                Log.d("adapter", "Work if isSelected")
                cardView.setCardBackgroundColor(Color.parseColor("#d6f0e8"))
                itemView.setBackgroundResource(R.drawable.dop_uslugi_selected_dop_item)
            } else {
                itemView.setBackgroundResource(R.drawable.dop_uslugi_unselected_item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DopUslugiViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dop_uslugi_item, parent, false)
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

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(
        newDopList: List<LitroCalculatorItems>,
    ) {
        dopItems = newDopList
        notifyDataSetChanged()
    }
}
