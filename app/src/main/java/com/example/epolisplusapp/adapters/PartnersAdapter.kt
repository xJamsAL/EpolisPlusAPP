package com.example.epolisplusapp.adapters

import android.content.Intent
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.epolisplusapp.R
import com.example.epolisplusapp.models.partners.GetPartnersResponse


class PartnersAdapter(private var partnersList: List<GetPartnersResponse> = listOf()) : RecyclerView.Adapter<PartnersAdapter.PartnersViewHolder>() {

    private val baseUrl = "https://epolisplus.uz/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.partners_item, parent, false)
        return PartnersViewHolder(view)
    }
    override fun onBindViewHolder(holder: PartnersViewHolder, position: Int) {
        val partner = partnersList[position]
        holder.bind(partner)
    }

    override fun getItemCount() = partnersList.size


    fun updateData(newPartnersList: List<GetPartnersResponse>) {
        partnersList = newPartnersList
        notifyDataSetChanged()
    }

    inner class PartnersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val btPhone: Button = itemView.findViewById(R.id.btCall)
        private val btSite: Button = itemView.findViewById(R.id.btWebsite)
        private val imageView: ImageView = itemView.findViewById(R.id.ivCompany)

        fun bind(partner: GetPartnersResponse) {
            btSite.setOnClickListener {
                val context = itemView.context
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(partner.site))
                context.startActivity(intent)
            }
            btPhone.setOnClickListener {
                val phoneNumber = partner.phone
                val cleanedPhoneNumber = phoneNumber.replace(Regex("[^0-9+]+"), "")
                if (cleanedPhoneNumber.isNotEmpty()) {
                        val context = itemView.context
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanedPhoneNumber"))
                        context.startActivity(intent)
                }
            }
            val fullImageUrl = "${baseUrl}${partner.image}"

            if (fullImageUrl.endsWith(".svg")) {
                Glide.with(itemView.context)
                    .`as`(PictureDrawable::class.java)
                    .load(fullImageUrl)
                    .error(R.drawable.auth_input_crossed_eye)
                    .into(imageView)
            } else {
                Glide.with(itemView.context)
                    .load(fullImageUrl)
                    .error(R.drawable.auth_input_crossed_eye)
                    .into(imageView)
            }
        }
    }
}
