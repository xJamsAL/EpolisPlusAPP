package com.example.epolisplusapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.epolisplusapp.R
import com.example.epolisplusapp.interfaces.IOnCarDeleteClickListener
import com.example.epolisplusapp.models.profile.CarInfo
import com.google.android.material.card.MaterialCardView

class CabinetAdapter(private var cardList: List<CarInfo>,    private val deleteClickListener: IOnCarDeleteClickListener) :
    RecyclerView.Adapter<CabinetAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avtoModel: TextView = itemView.findViewById(R.id.tvCarModel)
        val avtoYear: TextView = itemView.findViewById(R.id.tvCarYear)
        val avtoRegion: TextView = itemView.findViewById(R.id.tvAvtoRegion)
        val avtoNomer: TextView = itemView.findViewById(R.id.tvAvtoNomer)
        val techSeriya: TextView = itemView.findViewById(R.id.tvTechSeriya)
        val techNomer: TextView = itemView.findViewById(R.id.tvTechNomer)
        val orgName: TextView = itemView.findViewById(R.id.tvOwnerFullName)
        val btDeleteCar: MaterialCardView = itemView.findViewById(R.id.cardBtDeleteCar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cabinet_item, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount() = cardList.size
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val car = cardList[position]
        holder.avtoModel.text = car.MODEL_NAME
        holder.avtoYear.text = car.ISSUE_YEAR
        holder.techSeriya.text = car.TECH_SERIYA
        holder.techNomer.text = car.TECH_NUMBER
        holder.orgName.text = car.ORGNAME

        val govNumber = car.GOV_NUMBER
        if (govNumber.length > 2) {
            holder.avtoRegion.text = govNumber.substring(0, 2)
            holder.avtoNomer.text = govNumber.substring(2)
        } else {
            holder.avtoRegion.text = ""
            holder.avtoNomer.text = govNumber
        }
        holder.btDeleteCar.setOnClickListener {
            deleteClickListener.onDeleteCar(car)
        }


    }

    fun updateData(newCarList: List<CarInfo>) {
        cardList = newCarList
        notifyDataSetChanged()
    }



}