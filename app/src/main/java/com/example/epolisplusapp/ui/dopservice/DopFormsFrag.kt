package com.example.epolisplusapp.ui.dopservice


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epolisplusapp.R
import com.example.epolisplusapp.adapters.DopFormsAdapter
import com.example.epolisplusapp.databinding.DopUslugiOformlenBinding
import com.example.epolisplusapp.models.cabinet.AddCarRequest
import com.example.epolisplusapp.models.profile.CarInfo
import com.example.epolisplusapp.retrofit.RetrofitInstance
import com.example.epolisplusapp.util.AddAvtoFrag
import com.example.epolisplusapp.util.AddClientFrag
import com.example.epolisplusapp.util.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DopFormsFrag : BottomSheetDialogFragment(), CarDataListener {

    private lateinit var binding: DopUslugiOformlenBinding
    private lateinit var dopFormsViewModel: DopFormsViewModel
    private lateinit var dopFormsAdapter: DopFormsAdapter
    private var data: AddCarRequest? = null


    private var selectedCar: CarInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("1234", "onCreateView called")
        binding = DopUslugiOformlenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("1234", "onViewCreated called")
        super.onViewCreated(view, savedInstanceState)
        CommonUtils.setupToolbarDialog(binding.oformlenToolbar, this)
        CommonUtils.setupBottomSheetBehavior(view, this)

        val apiService = RetrofitInstance(requireContext()).api
        val sharedPreferences =
            requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val factory = DopFormsViewModelFactory(apiService, sharedPreferences)
        dopFormsViewModel = ViewModelProvider(this, factory)[DopFormsViewModel::class.java]
        binding.rcDopForms.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        dopFormsAdapter = DopFormsAdapter(emptyList()) { carInfo ->
            selectedCar = carInfo
            btBackground()
        }
        binding.rcDopForms.adapter = dopFormsAdapter

        dopFormsViewModel.carInfoLiveData.observe(viewLifecycleOwner) { carInfoList ->
            dopFormsAdapter.updateData(carInfoList)
        }

        dopFormsViewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            Log.e("1234", "Error received: $errorMessage")
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        dopFormsViewModel.carInfoLiveData.observe(viewLifecycleOwner) { carInfoList ->
        }


        val accessToken = sharedPreferences.getString("access_token", null)
        if (accessToken != null) {
            dopFormsViewModel.loadCarInfo(accessToken)
        }
        setupFragments()
        setupButtons()


    }


    private fun setupFragments() {
        val clientContainer = AddClientFrag()
        childFragmentManager.beginTransaction()
            .replace(R.id.clientDataContainer, clientContainer)
            .commit()

        val addAvtoFragment = AddAvtoFrag()
        childFragmentManager.beginTransaction()
            .replace(R.id.frag1, addAvtoFragment)
            .commit()
    }

    private fun setupButtons() {
        binding.btNext.setOnClickListener {
            if (selectedCar != null) {
                pressedTv()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Выберите элемент перед продолжением",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.apply {
            btBack.setOnClickListener {
                cItem1.setBackgroundResource(R.drawable.tv_rounded)
                ivItem1.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                cItem2.setBackgroundResource(R.drawable.tv_grey_back)
                btNext.visibility = View.VISIBLE
                container1.visibility = View.VISIBLE
                frag1.visibility = View.VISIBLE


                btLayout2.visibility = View.GONE
                clientDataContainer.visibility = View.GONE
                phoneContainer.visibility = View.GONE

                ivItem2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
                tvItem2.setTextColor(requireContext().getColor(R.color.grey))
            }
            btNext2.setOnClickListener {
                cItem2.setBackgroundResource(R.drawable.tv_not_rounded)
                ivItem2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
                cItem3.setBackgroundResource(R.drawable.tv_right_rounded)
                ivItem3.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                tvItem3.setTextColor(requireContext().getColor(R.color.black_text_color))
                confirmLayout.visibility = View.VISIBLE
                discountLayout.visibility = View.VISIBLE
                checkBoxLayout.visibility = View.VISIBLE
                btLayout2.visibility = View.GONE
                clientDataContainer.visibility = View.GONE
                phoneContainer.visibility = View.GONE
            }
            btBack2.setOnClickListener {
                cItem2.setBackgroundResource(R.drawable.tv_right_rounded)
                ivItem2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                cItem3.setBackgroundResource(R.drawable.tv_grey_back)
                ivItem3.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
                tvItem3.setTextColor(requireContext().getColor(R.color.grey))
                confirmLayout.visibility = View.GONE
                discountLayout.visibility = View.GONE
                checkBoxLayout.visibility = View.GONE
                btLayout2.visibility = View.VISIBLE
                clientDataContainer.visibility = View.VISIBLE
                phoneContainer.visibility = View.VISIBLE
            }
            btNext3.setOnClickListener {
                cItem3.setBackgroundResource(R.drawable.tv_not_rounded)
                ivItem3.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
                cItem4.setBackgroundResource(R.drawable.tv_right_rounded)
                ivItem4.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                tvItem4.setTextColor(requireContext().getColor(R.color.black_text_color))
                commonScrollview.visibility = View.GONE
                clickPaymeLayout.visibility = View.VISIBLE
            }
        }


    }

    private fun pressedTv() {
        Log.d("1234", "pressedTv called")
        binding.apply {
            cItem1.setBackgroundResource(R.drawable.tv_left_rounded)
            ivItem1.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
            cItem2.setBackgroundResource(R.drawable.tv_right_rounded)
            btNext.visibility = View.GONE
            container1.visibility = View.GONE
            frag1.visibility = View.GONE
            btLayout2.visibility = View.VISIBLE
            clientDataContainer.visibility = View.VISIBLE
            phoneContainer.visibility = View.VISIBLE
            data?.let { dopFormsViewModel.sendCarData2(it) }
            Log.d("1234", "pressedtv$data")
            ivItem2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            tvItem2.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_text_color))
        }
    }


    @SuppressLint("UseCompatTextViewDrawableApis")
    private fun btBackground() {
        binding.apply {
            btNext.compoundDrawableTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.white)
            btNext.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            btNext.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.green)
            btNext.strokeColor = ContextCompat.getColorStateList(requireContext(), R.color.green)
        }
    }

    override fun onCarDataReceived(addCarRequest: AddCarRequest) {
        data = addCarRequest
        Log.d("1234", "Data received from Frag1: $data")
    }


}
