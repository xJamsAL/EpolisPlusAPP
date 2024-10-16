package com.example.epolisplusapp.ui.dopservice


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epolisplusapp.R
import com.example.epolisplusapp.adapters.DopFormsAdapter
import com.example.epolisplusapp.databinding.DopUslugiOformlenBinding
import com.example.epolisplusapp.interfaces.ICarDataListener
import com.example.epolisplusapp.models.cabinet.request.AddCarRequest
import com.example.epolisplusapp.models.profile.CarInfo
import com.example.epolisplusapp.module.module_add_client.AddClientFrag
import com.example.epolisplusapp.module.module_add_avto.AddAvtoFrag
import com.example.epolisplusapp.util.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DopFormsFrag : BottomSheetDialogFragment(), ICarDataListener {

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
        dopFormsViewModel = DopFormsViewModel.create(requireContext())
        setupRecyclerView()
        setupFragments()
        setupButtons()
        setupObservers()
        dopFormsViewModel.loadCarInfo()
    }

    private fun setupObservers() {
        dopFormsViewModel.carInfoLiveData.observe(viewLifecycleOwner) { carInfoList ->
            dopFormsAdapter.updateData(carInfoList)
        }

        dopFormsViewModel.successLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Машина добавлена успешно", Toast.LENGTH_SHORT).show()
            binding.frag1.visibility = View.GONE
            binding.clientDataContainer.visibility = View.VISIBLE
        }

        dopFormsViewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            val message = errorMessage.getErrorMessage(requireContext())
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
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
        binding.apply {
            btGeneralInformationNext.setOnClickListener {
                if (selectedCar != null) {
                    navigateGeneralInfoNext()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Выберите элемент перед продолжением",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            setupNavigationButtons()
        }
    }

    private fun setupNavigationButtons() {
        binding.apply {
            btClientDataBack.setOnClickListener { navigateClientDataBack() }
            btClientDataNext.setOnClickListener { navigateClientDataNext() }
            btContractInfoBack.setOnClickListener { navigateContractInfoBack() }
            btContractInfoNext.setOnClickListener { navigateContractInfoNext() }
        }
    }

    private fun navigateClientDataBack() {
        binding.apply {
            cItem1.setBackgroundResource(R.drawable.tv_rounded)
            ivItem1.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            cItem2.setBackgroundResource(R.drawable.tv_grey_back)
            btGeneralInformationNext.visibility = View.VISIBLE
            container1.visibility = View.VISIBLE
            frag1.visibility = View.VISIBLE


            ClientDataLayout.visibility = View.GONE
            clientDataContainer.visibility = View.GONE
            phoneContainer.visibility = View.GONE

            ivItem2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
            tvItem2.setTextColor(requireContext().getColor(R.color.grey))
        }

    }

    private fun navigateClientDataNext() {
        binding.apply {
            cItem2.setBackgroundResource(R.drawable.tv_not_rounded)
            ivItem2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
            cItem3.setBackgroundResource(R.drawable.tv_right_rounded)
            ivItem3.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            tvItem3.setTextColor(requireContext().getColor(R.color.black_text_color))
            confirmLayout.visibility = View.VISIBLE
            discountLayout.visibility = View.VISIBLE
            contractInfoLayout.visibility = View.VISIBLE
            ClientDataLayout.visibility = View.GONE
            clientDataContainer.visibility = View.GONE
            phoneContainer.visibility = View.GONE
        }

    }

    private fun navigateContractInfoBack() {
        binding.apply {
            cItem2.setBackgroundResource(R.drawable.tv_right_rounded)
            ivItem2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            cItem3.setBackgroundResource(R.drawable.tv_grey_back)
            ivItem3.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
            tvItem3.setTextColor(requireContext().getColor(R.color.grey))
            confirmLayout.visibility = View.GONE
            discountLayout.visibility = View.GONE
            contractInfoLayout.visibility = View.GONE
            ClientDataLayout.visibility = View.VISIBLE
            clientDataContainer.visibility = View.VISIBLE
            phoneContainer.visibility = View.VISIBLE
        }

    }

    private fun navigateContractInfoNext() {
        binding.apply {
            cItem3.setBackgroundResource(R.drawable.tv_not_rounded)
            ivItem3.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
            cItem4.setBackgroundResource(R.drawable.tv_right_rounded)
            ivItem4.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            tvItem4.setTextColor(requireContext().getColor(R.color.black_text_color))
            commonScrollview.visibility = View.GONE
            clickPaymeLayout.visibility = View.VISIBLE
        }

    }

    private fun navigateGeneralInfoNext() {
        Log.d("1234", "pressedTv called")
        binding.apply {
            cItem1.setBackgroundResource(R.drawable.tv_left_rounded)
            ivItem1.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
            cItem2.setBackgroundResource(R.drawable.tv_right_rounded)
            btGeneralInformationNext.visibility = View.GONE
            container1.visibility = View.GONE
            frag1.visibility = View.GONE
            ClientDataLayout.visibility = View.VISIBLE
            clientDataContainer.visibility = View.VISIBLE
            phoneContainer.visibility = View.VISIBLE
            data?.let { dopFormsViewModel.sendCarData2(it) }
            Log.d("1234", "pressedtv$data")
            ivItem2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            tvItem2.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_text_color))
        }
    }

    private fun setupRecyclerView() {
        binding.rcDopForms.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        dopFormsAdapter = DopFormsAdapter(emptyList()) { carInfo ->
            selectedCar = carInfo
            updateButtonGeneralInfoNext()
            updateNextButtonState()
        }
        binding.rcDopForms.adapter = dopFormsAdapter
    }


    @SuppressLint("UseCompatTextViewDrawableApis")
    private fun updateButtonGeneralInfoNext() {
        binding.apply {
            btGeneralInformationNext.compoundDrawableTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.white)
            btGeneralInformationNext.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            btGeneralInformationNext.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.green)
            btGeneralInformationNext.strokeColor =
                ContextCompat.getColorStateList(requireContext(), R.color.green)
        }
    }

    private fun updateNextButtonState() {
        binding.btGeneralInformationNext.isEnabled = selectedCar != null
    }

    override fun onCarDataReceived(addCarRequest: AddCarRequest) {
        dopFormsViewModel.sendCarData2(addCarRequest)
    }


}
