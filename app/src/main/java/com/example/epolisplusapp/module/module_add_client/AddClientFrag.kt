package com.example.epolisplusapp.module.module_add_client

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.epolisplusapp.R
import com.example.epolisplusapp.databinding.ClientInputDataFragBinding
import com.example.epolisplusapp.util.CommonUtils
import com.example.epolisplusapp.util.CommonUtils.addDateMask
import com.example.epolisplusapp.util.EditHideKeyboard

class AddClientFrag : Fragment() {

    private lateinit var addClientFragViewModel: AddClientFragViewModel
    private lateinit var binding: ClientInputDataFragBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ClientInputDataFragBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addClientFragViewModel = AddClientFragViewModel.create(requireContext())
        setupButtons()
        setupUtils()
        setupObservers()
    }

    private fun setupObservers() {
        addClientFragViewModel.erroLiveData.observe(viewLifecycleOwner) { errorMessage ->
            val message = errorMessage.getErrorMessage(requireContext())
            CommonUtils.showCustomToast(requireContext(), message)
        }

        addClientFragViewModel.output1.observe(viewLifecycleOwner) { result ->

            result?.let {
                binding.apply {
                    loadDataBt.visibility = View.GONE
                    fullInfoContainer.visibility = View.VISIBLE
                    edPINFL.setText(result.pinfl)
                    edFullName.setText(result.fullName)
                }
                disableInputs()
            } ?: Log.d("1234", "Ne poluchilos")

            if (result == null) {
                Toast.makeText(requireContext(), "Ne poluchilos", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun setupButtons() {
        binding.apply {
            loadDataBt.setOnClickListener {
                binding.apply {
                    val input1 = dataInputEditText.text.toString().trim()
                    val input2 = nomerPassportInputEditText.text.toString().trim()
                    val input3 = seriyaPassportInputEditText.text.toString().trim()
                    addClientFragViewModel.testSendData(input1, input2, input3)
                }
            }
            addClientSbrosBt.setOnClickListener {
                addClientFragViewModel.clearClientData()
                loadDataBt.visibility = View.VISIBLE
                fullInfoContainer.visibility = View.GONE
                clearInputs()


            }
        }
    }


    private fun setupUtils() {
        val passportNomer = view?.findViewById<EditText>(R.id.nomerPassportInputEditText)
        passportNomer?.addTextChangedListener(EditHideKeyboard(passportNomer))
        view?.findViewById<EditText>(R.id.dataInputEditText)?.addDateMask()
        CommonUtils.setupEditTextsFrag(
            this,
            containerId = R.id.dataInputEditText,
            editTextIds = intArrayOf(
                R.id.dataInputEditText,
                R.id.seriyaPassportInputEditText,
                R.id.nomerPassportInputEditText
            ),

            limits = intArrayOf(10, 2, 7)
        )
    }

    private fun clearInputs() {
        binding.apply {
            dataInputEditText.text.clear()
            nomerPassportInputEditText.text.clear()
            seriyaPassportInputEditText.text.clear()
            edFullName.text.clear()
            edPINFL.text.clear()

            dataInputEditText.isEnabled = true
            nomerPassportInputEditText.isEnabled = true
            seriyaPassportInputEditText.isEnabled = true
        }
    }

    private fun disableInputs() {
        binding.apply {
            dataInputEditText.isEnabled = false
            nomerPassportInputEditText.isEnabled = false
            seriyaPassportInputEditText.isEnabled = false
            edPINFL.isEnabled = false
            edFullName.isEnabled = false

        }
    }
}