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
import com.example.epolisplusapp.databinding.AvtoNomerBinding
import com.example.epolisplusapp.models.BaseApiResponse
import com.example.epolisplusapp.models.cabinet.request.AddCarRequest
import com.example.epolisplusapp.models.cabinet.response.AddUserCarResponse
import com.example.epolisplusapp.util.CommonUtils
import com.example.epolisplusapp.util.EditHideKeyboard
import com.example.epolisplusapp.util.EditSpaces

class AddAvtoFrag : Fragment() {

    private lateinit var addAvtoViewModel: AddAvtoViewModel
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
        addAvtoViewModel = AddAvtoViewModel.create(requireContext())
        setupEditTexts()
        setupObservers()

        binding.apply {
            btLoadDataCom.setOnClickListener {
                val techSeriya = edTechSeriyaCom.text.toString().trim()
                val techNomer = edTechNomerCom.text.toString().trim()
                val avtoRegion = edRegion.text.toString().trim()
                val avtoNomer = edAvtoNomer.text.toString().trim()

                addAvtoViewModel.sendCarData(techSeriya, techNomer, avtoRegion, avtoNomer)
                loadLayoutCom.visibility = View.VISIBLE
            }

            btSbros.setOnClickListener {
                addAvtoViewModel.resetData()
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
        // Наблюдение за ошибками
        addAvtoViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { failure ->
            failure?.let {
                val message = it.getErrorMessage(requireContext())
                Log.d("1234", "Ошибка: $message")  // Лог при возникновении ошибки
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        // Наблюдение за успешным сообщением
        addAvtoViewModel.successMessageLiveData.observe(viewLifecycleOwner) { success ->
            Log.d("1234", "Успешное получение данных")  // Лог при успехе
        }

        // Наблюдение за данными автомобиля
        addAvtoViewModel.addCarRequestLiveData.observe(viewLifecycleOwner) { carData ->
            if (carData != null) {
                Log.d("1234", "Данные автомобиля получены: $carData")  // Лог при получении данных
                updateUIWithCarData(carData)
                binding.loadLayoutCom.visibility = View.GONE
                binding.insideContainerCom.visibility = View.VISIBLE
            } else {
                Log.d("1234", "Данные автомобиля отсутствуют")  // Лог при отсутствии данных
                Toast.makeText(requireContext(), "Ошибка: данные не получены", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUIWithCarData(carData: AddCarRequest) {
        Log.d("1234", "Обновление UI с данными автомобиля: ${carData.ORGNAME}, ${carData.MODEL_NAME}, ${carData.ISSUE_YEAR}")
        binding.apply {
            edOrgNameCom.setText(carData.ORGNAME)
            edAvtoMarkCom.setText(carData.MODEL_NAME)
            edAvtoYearCom.setText(carData.ISSUE_YEAR)
        }
        disableFields()
        Log.d("1234", "Поля ввода отключены")  // Лог после отключения полей ввода
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

