package com.example.epolisplusapp.ui.dopservice.dialog


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.epolisplusapp.R
import com.example.epolisplusapp.databinding.DopUslugiOformlenBinding
import com.example.epolisplusapp.module.module_add_avto.AddAvtoFrag
import com.example.epolisplusapp.module.module_add_client.AddClientFrag
import com.example.epolisplusapp.util.CommonUtils
import com.example.epolisplusapp.util.EditHideKeyboard
import com.example.epolisplusapp.util.PhoneNumberMaskWatcher
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DopFormsDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DopUslugiOformlenBinding
    private lateinit var dopFormsDialogViewModel: DopFormsDialogViewModel
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
        dopFormsDialogViewModel = DopFormsDialogViewModel.create(requireContext())

        setupFragments()
        setupObservers()
        setupButtons()
        setupNavigationButtons()
        setupUtils(view)
    }


    private fun setupUtils(view: View) {
        CommonUtils.setupBottomSheetBehavior(view, this)
        CommonUtils.setupToolbarDialog(binding.oformlenToolbar, this)
        CommonUtils.hidekeyboardDialog(this)
        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                dopFormsDialogViewModel.setPhoneText(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {
            }

        }
        binding.apply {
            edPhoneInput.addTextChangedListener(EditHideKeyboard(edPhoneInput))
            edPhoneInput.addTextChangedListener(
                PhoneNumberMaskWatcher(" (##) ###-##-##", edPhoneInput)
            )
            edPhoneInput.addTextChangedListener(textWatcher)
        }
    }

    private fun setupObservers() {
        dopFormsDialogViewModel.navigateBoolean.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                navigateGeneralInfoNext()
            }
        }

        dopFormsDialogViewModel.isButtonEnabled.observe(viewLifecycleOwner){ isEnabled ->
            if (isEnabled){
                updateUIBtClientDataNext()
            }
            else{
                updateUIBtClientDataAfterReset()
            }
        }
        dopFormsDialogViewModel.carDataFromShareidViewModel.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                updateUIBtGeneralInfoNext()
            }
            if (data == null) {
                updateUIBtGeneralInfoNextAfterReset()
            }
        }
//        dopFormsViewModel.clientDataFromSharedViewModel.observe(viewLifecycleOwner) { data ->
//            if (data != null) {
//                updateUIBtClientDataNext()
//            }
//            if (data == null) {
//                updateUIBtClientDataAfterReset()
//            }
//        }
        dopFormsDialogViewModel.clientBoolean.observe(viewLifecycleOwner) { isSucces ->
            if (isSucces) {
                navigateClientDataNext()
            }
        }
        dopFormsDialogViewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            val message = errorMessage.getErrorMessage(requireContext())
            CommonUtils.showCustomToast(requireContext(), message)
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
                dopFormsDialogViewModel.callOnClickBtn()
                navigateGeneralInfoNext()
            }
            btClientDataBack.setOnClickListener {
                navigateClientDataBack()
            }
            setupNavigationButtons()
            btClientDataNext.setOnClickListener {
                dopFormsDialogViewModel.validateAndInput(binding.edPhoneInput.text.toString().trim())
            }

        }
    }

    private fun setupNavigationButtons() {
        binding.apply {
//            btClientDataNext.setOnClickListener { navigateClientDataNext() }
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
        binding.apply {

            cGeneralInfoLayout.setBackgroundResource(R.drawable.tv_left_rounded)
            ivGeneralInfoIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
            cClientDataLayout.setBackgroundResource(R.drawable.tv_right_rounded)
            btGeneralInformationNext.visibility = View.GONE
            CarListContainer.visibility = View.GONE
            frag1.visibility = View.GONE
            ClientDataLayout.visibility = View.VISIBLE
            clientDataContainer.visibility = View.VISIBLE
            phoneContainer.visibility = View.VISIBLE
            ivClientDataIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            tvClientDataText.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black_text_color
                )
            )
        }
    }


    private fun updateUIBtGeneralInfoNext() {
        CommonUtils.updateButtonStyle(
            requireContext(),
            binding.btGeneralInformationNext,
            CommonUtils.ButtonStyle.ENABLED
        )
    }

    private fun updateUIBtGeneralInfoNextAfterReset() {
        CommonUtils.updateButtonStyle(
            requireContext(),
            binding.btGeneralInformationNext,
            CommonUtils.ButtonStyle.DISABLED
        )
    }

    private fun updateUIBtClientDataNext() {
        CommonUtils.updateButtonStyle(
            requireContext(),
            binding.btClientDataNext,
            CommonUtils.ButtonStyle.ENABLED
        )
    }

    private fun updateUIBtClientDataAfterReset() {

        CommonUtils.updateButtonStyle(
            requireContext(),
            binding.btClientDataNext,
            CommonUtils.ButtonStyle.DISABLED
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dopFormsDialogViewModel.clearShareData()
    }

}
