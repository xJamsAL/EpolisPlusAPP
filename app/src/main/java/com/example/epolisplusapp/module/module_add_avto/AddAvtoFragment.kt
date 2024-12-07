package com.example.epolisplusapp.module.module_add_avto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.epolisplusapp.R
import com.example.epolisplusapp.databinding.AvtoNomerBinding
import com.example.epolisplusapp.models.cabinet.request.AddCarRequest
import com.example.epolisplusapp.models.error_models.ApiErrorMessage
import com.example.epolisplusapp.models.error_models.GenericFailure
import com.example.epolisplusapp.models.error_models.NetworkFailure
import com.example.epolisplusapp.ui.dopservice.dialog.DopFormsDialogViewModel
import com.example.epolisplusapp.util.CommonUtils
import com.example.epolisplusapp.util.EditHideKeyboard
import com.example.epolisplusapp.util.EditSpaces

class AddAvtoFragment : Fragment() {

    private lateinit var addAvtoViewModel: AddAvtoViewModel
    private lateinit var dopFormsDialogViewModel: DopFormsDialogViewModel

    private var _binding: AvtoNomerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AvtoNomerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val carNomer: EditText = view.findViewById(R.id.edAvtoNomer)
        carNomer.addTextChangedListener(EditSpaces(carNomer))
        binding.edTechNomerCom.addTextChangedListener(EditHideKeyboard(binding.edTechNomerCom))
        dopFormsDialogViewModel = DopFormsDialogViewModel.create(requireContext())
        addAvtoViewModel = AddAvtoViewModel.create(requireContext())
        setupEditTexts()
        setupObservers()
        binding.apply {
            btLoadDataCom.setOnClickListener {
                val techSeriya = edTechSeriyaCom.text.toString().trim()
                val techNomer = edTechNomerCom.text.toString().trim()
                val avtoRegion = edRegion.text.toString().trim()
                val avtoNomer = edAvtoNomer.text.toString().trim()
                addAvtoViewModel.callOnClickLoadData(avtoRegion, avtoNomer, techSeriya, techNomer)

            }

            btSbros.setOnClickListener {
                addAvtoViewModel.clearedData()
                binding.loadLayoutCom.visibility = View.VISIBLE
                binding.insideContainerCom.visibility = View.GONE
                clearInputFields()
            }
        }
    }

    private fun setupEditTexts() {
        CommonUtils.setupEditTextsFrag(
            this,
            containerId = R.id.tvCarNomer,
            editTextIds = intArrayOf(
                R.id.edRegion,
                R.id.edAvtoNomer,
                R.id.edTechSeriyaCom,
                R.id.edTechNomerCom
            ),
            limits = intArrayOf(2, 8, 3, 7)
        )
    }

    private fun setupObservers() {

        addAvtoViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { failure ->
            if (failure is ApiErrorMessage)
                CommonUtils.showCustomToast(
                    requireContext(),
                    failure.getErrorMessage(requireContext())
                )
            if (failure is GenericFailure)
                CommonUtils.showCustomToast(
                    requireContext(),
                    failure.getErrorMessage(requireContext())
                )
            if (failure is NetworkFailure)
                CommonUtils.showCustomToast(
                    requireContext(),
                    failure.getErrorMessage(requireContext())
                )
            val message = failure.getErrorMessage(requireContext())
            CommonUtils.showCustomToast(requireContext(), message)
        }
        addAvtoViewModel.navigateBoolean.observe(viewLifecycleOwner) { isSucces ->
            if (isSucces == true) {
                binding.apply {
                    val techSeriya = edTechSeriyaCom.text.toString().trim()
                    val techNomer = edTechNomerCom.text.toString().trim()
                    val avtoRegion = edRegion.text.toString().trim()
                    val avtoNomer = edAvtoNomer.text.toString().trim()
                    addAvtoViewModel.sendCarData(techSeriya, techNomer, avtoRegion, avtoNomer)
                    loadLayoutCom.visibility = View.VISIBLE
                }
            }

        }


        addAvtoViewModel.addCarRequestLiveData.observe(viewLifecycleOwner) { carData ->
            if (carData != null) {
                updateUIWithCarData(carData)
                binding.loadLayoutCom.visibility = View.GONE
                binding.insideContainerCom.visibility = View.VISIBLE
            }
        }
    }

    private fun updateUIWithCarData(carData: AddCarRequest) {
        binding.apply {
            edOrgNameCom.setText(carData.ORGNAME)
            edAvtoMarkCom.setText(carData.MODEL_NAME)
            edAvtoYearCom.setText(carData.ISSUE_YEAR)
        }
        disableFields()
    }


    private fun clearInputFields() {
        binding.apply {
            edAvtoMarkCom.text.clear()
            edAvtoYearCom.text.clear()
            edOrgNameCom.text.clear()
            edTechNomerCom.text.clear()
            edTechSeriyaCom.text.clear()
            edRegion.text.clear()
            edAvtoNomer.text.clear()

            edRegion.isEnabled = true
            edTechNomerCom.isEnabled = true
            edTechSeriyaCom.isEnabled = true
        }
    }

    private fun disableFields() {
        binding.apply {
            edOrgNameCom.isEnabled = false
            edAvtoMarkCom.isEnabled = false
            edAvtoYearCom.isEnabled = false
            edAvtoNomer.isEnabled = false
            edRegion.isEnabled = false
            edTechNomerCom.isEnabled = false
            edTechSeriyaCom.isEnabled = false
        }
    }
}

