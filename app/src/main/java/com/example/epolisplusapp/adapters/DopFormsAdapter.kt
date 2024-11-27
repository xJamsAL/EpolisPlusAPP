package com.example.epolisplusapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.epolisplusapp.R
import com.example.epolisplusapp.models.profile.CarInfo

class DopFormsAdapter(
    private var cardList: List<CarInfo>,
    private val onItemSelected: (CarInfo) -> Unit
) : RecyclerView.Adapter<DopFormsAdapter.DopViewHolder>() {
    private var selectedItem: CarInfo? = null
    private var onItemClickListener: ((CarInfo) -> Unit)? = null

    inner class DopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avtoTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val avtoRegion: TextView = itemView.findViewById(R.id.tvRegion)
        val avtoNomer: TextView = itemView.findViewById(R.id.tvNomer)

        init {
            itemView.setOnClickListener {
                selectedItem = cardList[adapterPosition]
                notifyDataSetChanged()
                onItemSelected(selectedItem!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DopViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.car_item_lite, parent, false)
        return DopViewHolder(view)
    }

    override fun getItemCount() = cardList.size

    override fun onBindViewHolder(holder: DopViewHolder, position: Int) {
        val car = cardList[position]
        holder.avtoTitle.text = car.MODEL_NAME
        val govNumber = car.GOV_NUMBER
        if (govNumber.length > 2) {
            holder.avtoRegion.text = govNumber.substring(0, 2)
            holder.avtoNomer.text = govNumber.substring(2)
        } else {
            holder.avtoRegion.text = ""
            holder.avtoNomer.text = govNumber
        }
        if (car == selectedItem) {
            holder.itemView.setBackgroundResource(R.drawable.item_changed) // Фон для выбранного элемента
        } else {
            holder.itemView.setBackgroundResource(R.drawable.auth_input_border) // Обычный фон
        }
    }

    fun setSelectedItem(carInfo: CarInfo) {
        selectedItem = carInfo
        notifyDataSetChanged()
    }

    fun getSelectedItem(): CarInfo? {
        return selectedItem
    }

    fun updateData(newCarList: List<CarInfo>) {
        cardList = newCarList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (CarInfo) -> Unit) {
        onItemClickListener = listener
    }
}
