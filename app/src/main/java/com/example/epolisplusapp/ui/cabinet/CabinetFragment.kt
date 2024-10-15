package com.example.epolisplusapp.ui.cabinet

import PersonalDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.epolisplusapp.R
import com.example.epolisplusapp.adapters.CabinetAdapter
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.interfaces.IOnCarDeleteClickListener
import com.example.epolisplusapp.models.cabinet.request.DeleteCarRequest
import com.example.epolisplusapp.models.profile.CarInfo
import com.example.epolisplusapp.models.profile.SharedViewModel
import com.example.epolisplusapp.service.RetrofitInstance
import com.example.epolisplusapp.util.CommonUtils
import com.example.epolisplusapp.util.applyPhoneNumberMask
import com.google.android.material.appbar.MaterialToolbar
import eightbitlab.com.blurview.BlurView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CabinetFragment : Fragment(), IOnCarDeleteClickListener {
    private lateinit var apiService: MainApi.ApiService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var cabinetAdapter: CabinetAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAvto: CardView
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var progressBarBackPart: BlurView
    private lateinit var recycleScroll: LinearLayout
    private lateinit var btCardAddAvto2 : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.cabinet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rcCarinfo)
        cardAvto = view.findViewById(R.id.cardAddAvto)
        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbarCabinet)
        val userFullName: TextView = view.findViewById(R.id.tvFullName)
        val userPhone: TextView = view.findViewById(R.id.tvPhone)
        recycleScroll = view.findViewById(R.id.recycleScroll)
        btCardAddAvto2 = view.findViewById(R.id.btCardAvto2)


        recyclerView.layoutManager = LinearLayoutManager(context)


        cabinetAdapter = CabinetAdapter(emptyList(), this)
        recyclerView.adapter = cabinetAdapter


        val activity = requireActivity()
        progressBarBackPart = activity.findViewById(R.id.progressBarBackPart)

        apiService = RetrofitInstance(requireContext()).api
        sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        activity.window?.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = android.graphics.Color.TRANSPARENT
        }

        val btAddAvto: Button = view.findViewById(R.id.btCardAddAvto)

        btAddAvto.setOnClickListener {
            val addAvtoDialog = AddAvtoDialog()
            addAvtoDialog.show(parentFragmentManager, addAvtoDialog.tag)
        }
        btCardAddAvto2.setOnClickListener {
            val addAvtoDialog = AddAvtoDialog()
            addAvtoDialog.show(parentFragmentManager, addAvtoDialog.tag)
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit_bt -> {
                    val personalDialog = PersonalDialog()
                    personalDialog.show(parentFragmentManager, personalDialog.tag)

                    true
                }

                else -> false
            }
        }
        loadUserProfile(userFullName, userPhone)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        val nameSend = userFullName.text.toString()
        val phoneSend = userPhone.text.toString()
        sharedViewModel.setTextData(nameSend)
        sharedViewModel.setPhoneData(phoneSend)


    }




    private fun loadUserProfile(
        userFullName: TextView,
        userPhone: TextView
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    progressBarBackPart.visibility = View.VISIBLE
                }

                val accessToken = sharedPreferences.getString("access_token", null)
                if (accessToken != null) {
                    val response = apiService.getUserProfile("Bearer $accessToken")
                    val carInfo = response.response.car_info

                    withContext(Dispatchers.Main) {
                        userFullName.text = response.response.full_name

                        val mask = "### (##) ###-##-##"
                        val formattedPhoneNumber =
                            applyPhoneNumberMask(response.response.phone, mask)
                        userPhone.text = formattedPhoneNumber

                        sharedViewModel.setTextData(response.response.full_name)
                        sharedViewModel.setPhoneData(response.response.phone)


                        if (carInfo.isEmpty()) {
                            recycleScroll.visibility = View.GONE
                            cardAvto.visibility = View.VISIBLE
                        } else {
                            recycleScroll.visibility = View.VISIBLE
                            cardAvto.visibility = View.GONE
                            cabinetAdapter.updateData(carInfo)
                        }

                        progressBarBackPart.visibility = View.GONE
                    }
                } else {

                    withContext(Dispatchers.Main) {
                        progressBarBackPart.visibility = View.GONE
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    progressBarBackPart.visibility = View.GONE
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBarBackPart.visibility = View.GONE
                    e.printStackTrace()
                }
            }
        }
    }



    override fun onDeleteCar(car: CarInfo) {
        deleteCarFromServer(car.GOV_NUMBER)
    }

    private fun deleteCarFromServer(carNumber: String) {
        val deleteCarRequest = DeleteCarRequest(carNumber)
        lifecycleScope.launch {
            progressBarBackPart.visibility = View.VISIBLE
            try {
                val accessToken = sharedPreferences.getString("access_token", null)
                val response = apiService.deleteCar("Bearer $accessToken", deleteCarRequest)
                if (response.status == 200 && response.code) {
                    Toast.makeText(context, "Машина успешно удалена", Toast.LENGTH_SHORT).show()
                    refreshCarList()
                } else {
                    Toast.makeText(context, "Ошибка: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Ошибка сети: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {

                CommonUtils.reloadFragment(requireActivity().supportFragmentManager, CabinetFragment())
                progressBarBackPart.visibility = View.GONE
            }
        }
    }


    private fun refreshCarList() {
    }

    companion object {
        @JvmStatic
        fun newInstance() = CabinetFragment()
    }
}
