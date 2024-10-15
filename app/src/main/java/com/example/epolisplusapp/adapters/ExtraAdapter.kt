package com.example.epolisplusapp.adapters

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.epolisplusapp.R
import com.example.epolisplusapp.models.emergency.EmergencyServiceRisk
import com.example.epolisplusapp.util.CommonUtils
import java.util.Locale

class ExtraAdapter(private var riskItem: List<EmergencyServiceRisk>,
                   private val currencyResId: Int,
                   private val context: Context) :
    RecyclerView.Adapter<ExtraAdapter.ExtraViewHolder>() {
    inner class ExtraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.ivExtraIcon)
        val name: TextView = itemView.findViewById(R.id.tvExtraTitle)
        val price: TextView = itemView.findViewById(R.id.tvExtraSubtitle)
        fun bind(extra: EmergencyServiceRisk) {
            val formated =CommonUtils.formatSumWithSeparatorAndCurrency(
                extra.price, Locale.getDefault(), context, currencyResId
            )
            price.text = formated
            name.text = extra.name
            val image = extra.icon
           if (image.endsWith(".svg")){
               Glide.with(itemView.context)
                   .`as`(PictureDrawable::class.java)
                   .load(image)
                   .error(R.drawable.auth_input_crossed_eye)
                   .into(icon)
           }else{
               Glide.with(itemView.context)
                   .load(image)
                   .error(R.drawable.auth_input_crossed_eye)
                   .into(icon)
           }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtraViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.extra_item, parent, false)
        return ExtraViewHolder(view)
    }

    override fun getItemCount() = riskItem.size

    override fun onBindViewHolder(holder: ExtraViewHolder, position: Int) {
        val extra = riskItem[position]
        holder.bind(extra)


    }

    fun updateData(newExtraList: List<EmergencyServiceRisk>) {
        riskItem = newExtraList
        notifyDataSetChanged()
    }
}