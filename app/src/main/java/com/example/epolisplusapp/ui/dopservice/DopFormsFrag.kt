package com.example.epolisplusapp.ui.dopservice


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.epolisplusapp.R
import com.example.epolisplusapp.databinding.DopUslugiOformlenBinding
import com.example.epolisplusapp.module.module_add_avto.AddAvtoFrag
import com.example.epolisplusapp.module.module_add_client.AddClientFrag
import com.example.epolisplusapp.util.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DopFormsFrag : BottomSheetDialogFragment() {

    private lateinit var binding: DopUslugiOformlenBinding
    private lateinit var dopFormsViewModel: DopFormsViewModel
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
        setupFragments()
        setupObservers()
        setupButtons()
        setupNavigationButtons()
    }


    private fun setupObservers() {

        dopFormsViewModel.navigateGeneralInfoNext.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate == true) {
                Log.d("1234", "asdsad$shouldNavigate")
                navigateGeneralInfoNext()
                dopFormsViewModel.resetNavigation()
            } else {
                Log.d("1234", "Ошибка в наблюдении за navigateGeneralInfoNext")
            }
        }

        dopFormsViewModel.carDataReceived.observe(viewLifecycleOwner){data ->
            Log.d("1234", "carDataReceived observe frag = ${data} ")
            if (data != null) {
                Log.d("1234", "poluchilos")
                dopFormsViewModel.callOnClickBtn()
            }
        }

        dopFormsViewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->

            val message = errorMessage.getErrorMessage(requireContext())
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("CommitTransaction")
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
                dopFormsViewModel.callOnClickBtn()
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
            cGeneralInfoLayout.setBackgroundResource(R.drawable.tv_rounded)
            ivGeneralInfoIcon.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            cClientDataLayout.setBackgroundResource(R.drawable.tv_grey_back)
            btGeneralInformationNext.visibility = View.VISIBLE
            CarListContainer.visibility = View.VISIBLE
            frag1.visibility = View.VISIBLE


            ClientDataLayout.visibility = View.GONE
            clientDataContainer.visibility = View.GONE
            phoneContainer.visibility = View.GONE

            ivClientDataIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
            tvClientDataText.setTextColor(requireContext().getColor(R.color.grey))
        }

    }

    private fun navigateClientDataNext() {
        binding.apply {
            cClientDataLayout.setBackgroundResource(R.drawable.tv_not_rounded)
            ivClientDataIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
            cContractInfoLayout.setBackgroundResource(R.drawable.tv_right_rounded)
            ivContractInfoIcon.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            tvContractInfoText.setTextColor(requireContext().getColor(R.color.black_text_color))
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
            cClientDataLayout.setBackgroundResource(R.drawable.tv_right_rounded)
            ivClientDataIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            cContractInfoLayout.setBackgroundResource(R.drawable.tv_grey_back)
            ivContractInfoIcon.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )
            tvContractInfoText.setTextColor(requireContext().getColor(R.color.grey))
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
            cContractInfoLayout.setBackgroundResource(R.drawable.tv_not_rounded)
            ivContractInfoIcon.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )
            cPayLayout.setBackgroundResource(R.drawable.tv_right_rounded)
            ivPayIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            tvPayText.setTextColor(requireContext().getColor(R.color.black_text_color))
            commonScrollview.visibility = View.GONE
            clickPaymeLayout.visibility = View.VISIBLE
        }

    }

    private fun navigateGeneralInfoNext() {
        Log.d("1234", "navigateGeneralInfoNext called")
        binding.apply {
//            dopFormsViewModel.test()
            cGeneralInfoLayout.setBackgroundResource(R.drawable.tv_left_rounded)
            ivGeneralInfoIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
            cClientDataLayout.setBackgroundResource(R.drawable.tv_right_rounded)
            btGeneralInformationNext.visibility = View.GONE
            CarListContainer.visibility = View.GONE
            frag1.visibility = View.GONE
            ClientDataLayout.visibility = View.VISIBLE
            clientDataContainer.visibility = View.VISIBLE
            phoneContainer.visibility = View.VISIBLE

            Log.d("1234", "model = $dopFormsViewModel")
            ivClientDataIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            tvClientDataText.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black_text_color
                )
            )
        }
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


//    override fun onDestroyView() {
//        super.onDestroyView()
//        dopFormsViewModel.carInfoLiveData.removeObservers(viewLifecycleOwner)
//        dopFormsViewModel.successLiveData.removeObservers(viewLifecycleOwner)
//        dopFormsViewModel.errorLiveData.removeObservers(viewLifecycleOwner)
//        dopFormsViewModel.carData.removeObservers(viewLifecycleOwner)
//        binding.rcDopForms.adapter = null
//
//    }

}
