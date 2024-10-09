package com.example.epolisplusapp.ui.partners

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.epolisplusapp.R
import com.example.epolisplusapp.adapters.PartnersAdapter
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.retrofit.RetrofitInstance
import eightbitlab.com.blurview.BlurView
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PartnersFragment : Fragment() {
    private lateinit var partnersAdapter: PartnersAdapter
    private lateinit var apiService: MainApi.ApiService
    private lateinit var progressBarBackPart: BlurView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.partneers_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rcPartners)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val activity = requireActivity()
        progressBarBackPart = activity.findViewById(R.id.progressBarBackPart)

        partnersAdapter = PartnersAdapter()
        recyclerView.adapter = partnersAdapter
        apiService = RetrofitInstance(requireContext()).api


        activity.window?.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = android.graphics.Color.TRANSPARENT
        }
        loadPartnersData()
    }

    private fun loadPartnersData() {
        lifecycleScope.launch {
            try {

                progressBarBackPart.visibility = View.VISIBLE
                val response = apiService.getPartners()
                partnersAdapter.updateData(response.response)
                progressBarBackPart.visibility = View.GONE
            } catch (e: HttpException) {

                progressBarBackPart.visibility = View.GONE
                e.printStackTrace()
            } catch (e: Exception) {

                progressBarBackPart.visibility = View.GONE
                e.printStackTrace()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PartnersFragment()
    }
}
