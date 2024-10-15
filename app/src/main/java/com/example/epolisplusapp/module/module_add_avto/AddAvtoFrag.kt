package com.example.epolisplusapp.module.module_add_avto

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.epolisplusapp.R
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.databinding.AvtoNomerBinding
import com.example.epolisplusapp.service.PreferenceService
import com.example.epolisplusapp.service.RetrofitInstance
import com.example.epolisplusapp.util.CommonUtils
import com.example.epolisplusapp.util.EditHideKeyboard
import com.example.epolisplusapp.util.EditSpaces
import com.google.gson.ToNumberStrategy

class AddAvtoFrag : Fragment() {

    private lateinit var addAvtoViewModel: AddAvtoViewModel
    private lateinit var apiService: MainApi.ApiService
    private lateinit var preferenceService: PreferenceService
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

        val carNomer: EditText = view.findViewById(R.id.edAvtoNomer)
        carNomer.addTextChangedListener(EditSpaces(carNomer))
        binding.edTechNomerCom.addTextChangedListener(EditHideKeyboard(binding.edTechNomerCom))
        preferenceService = PreferenceService.getInstance(requireContext())
        apiService = RetrofitInstance(requireContext()).api
        addAvtoViewModel = AddAvtoViewModel(apiService, preferenceService)

        setupObservers()

        addAvtoViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { failure ->
            failure?.let {
                val message = it.getErrorMessage(requireContext())
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
        binding.apply {
            btLoadDataCom.setOnClickListener {
                val techSeriya = edTechSeriyaCom.text.toString().trim()
                val techNomer = edTechNomerCom.text.toString().trim()
                val avtoRegion = edRegion.text.toString().trim()
                val avtoNomer = edAvtoNomer.text.toString().trim()

                if (validateInput(techSeriya, techNomer, avtoRegion, avtoNomer)) {
                    Log.d(
                        "1234",
                        "Sending car data: $techSeriya, $techNomer, $avtoRegion, $avtoNomer"
                    )
                    addAvtoViewModel.sendCarData(techSeriya, techNomer, avtoRegion, avtoNomer)
                    binding.loadLayoutCom.visibility = View.VISIBLE
                }
            }

            btSbros.setOnClickListener {
                addAvtoViewModel.resetData()
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
    }

    private fun setupObservers() {
        addAvtoViewModel.carDataLiveData.observe(viewLifecycleOwner) { carData ->
            if (carData != null) {
                Log.d(
                    "1234",
                    "Received car data: ${carData.response.ORGNAME}, ${carData.response.MODEL_NAME}, ${carData.response.ISSUE_YEAR}"
                )
                binding.edOrgNameCom.setText(carData.response.ORGNAME)
                binding.edAvtoMarkCom.setText(carData.response.MODEL_NAME)
                binding.edAvtoYearCom.setText(carData.response.ISSUE_YEAR)

                disableFields()
                binding.loadLayoutCom.visibility = View.GONE
                binding.insideContainerCom.visibility = View.VISIBLE
            }
        }
        addAvtoViewModel.isContainerVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.insideContainerCom.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        addAvtoViewModel.isSecondContainerVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.loadLayoutCom.visibility = if (isVisible) View.GONE else View.VISIBLE
        }
    }


    private fun validateInput(
        techSeriya: String,
        techNomer: String,
        avtoRegion: String,
        avtoNomer: String
    ): Boolean {
        return when {
            techSeriya.isEmpty() || techNomer.isEmpty() || avtoRegion.isEmpty() || avtoNomer.isEmpty() -> {
                Log.d("1234", "Fields are empty")
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                false
            }

            techSeriya.length < 3 -> {
                Log.d("1234", "Invalid techSeriya length")
                Toast.makeText(
                    requireContext(),
                    "Неправильная серия тех. паспорта",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            techNomer.length < 7 -> {
                Log.d("1234", "Invalid techNomer length")
                Toast.makeText(
                    requireContext(),
                    "Неправильный номер тех. паспорта",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            avtoRegion.length < 2 || avtoNomer.length < 7 -> {
                Log.d("1234", "Invalid car number or region length")
                Toast.makeText(requireContext(), "Неправильный номер машины", Toast.LENGTH_SHORT)
                    .show()
                false
            }

            else -> true
        }
    }

    private fun disableFields() {
        Log.d("1234", "Disabling input fields")
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
