package com.example.epolisplusapp.ui.cabinet

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.epolisplusapp.R
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.databinding.CabinetDialogAddAvtoBinding
import com.example.epolisplusapp.models.cabinet.AddCarRequest
import com.example.epolisplusapp.models.cabinet.CheckCarRequest
import com.example.epolisplusapp.models.cabinet.Response
import com.example.epolisplusapp.retrofit.RetrofitInstance
import com.example.epolisplusapp.util.CommonUtils
import com.example.epolisplusapp.util.EditHideKeyboard
import com.example.epolisplusapp.util.EditSpaces
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import eightbitlab.com.blurview.BlurView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AddAvtoDialog: BottomSheetDialogFragment() {
    private lateinit var apiService: MainApi.ApiService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressBar: BlurView
    private var _binding: CabinetDialogAddAvtoBinding? = null
    private val binding get() = _binding!!
    private var savedResponse: Response? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CabinetDialogAddAvtoBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progBarDialog
        CommonUtils.setupBlurViewForFragment(this, progressBar)
        CommonUtils.setupBottomSheetBehavior(view,this)
        CommonUtils.setupToolbarDialog(binding.toolbarDialog, this)
        CommonUtils.setupEditTextsDialog(this,
            containerId = R.id.tvAvtoNomerRegion,
            editTextIds = intArrayOf(
                R.id.edAddAvtoRegion,
                R.id.edAddAvtoNomer,
                R.id.edTechSeriya,
                R.id.edTechNomer
            ),
            limits = intArrayOf(2, 8, 3, 7 )
            )
        sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        apiService = RetrofitInstance(requireContext()).api

        binding.tvInfoTech.setOnClickListener {
            val techInfoDialog = TechInfoDialog()
            techInfoDialog.show(parentFragmentManager, techInfoDialog.tag)
        }

        binding.btLoadData.setOnClickListener {
            val techSeriya = binding.edTechSeriya.text.toString().trim()
            val techNomer = binding.edTechNomer.text.toString().trim()
            val avtoRegion = binding.edAddAvtoRegion.text.toString().trim()
            val avtoNomer = binding.edAddAvtoNomer.text.toString().trim()

            if (techSeriya.isEmpty() || techNomer.isEmpty() || avtoRegion.isEmpty() || avtoNomer.isEmpty()){

                Toast.makeText(requireContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            } else if (techSeriya.length < 3 ){
                Toast.makeText(requireContext(), "Пожалуйста, заполните серию тех. паспорта", Toast.LENGTH_SHORT).show()
            } else if (techNomer.length < 7  ){
                Toast.makeText(requireContext(), "Пожалуйста, заполните номер тех. паспорта", Toast.LENGTH_SHORT).show()
            } else if (avtoRegion.length < 2 || avtoNomer.length < 8 && avtoNomer.length < 7 ){
                Toast.makeText(requireContext(), "Пожалуйста, заполните номер машины", Toast.LENGTH_SHORT).show()
            } else{
                CommonUtils.hideKeyboard(this)
                sendCarData(techSeriya, techNomer, avtoRegion, avtoNomer)

            }
            binding.btAddCar.setOnClickListener {
                sendCarDataToSecondAPI()

            }

        }
        val avtoNomer : EditText = view.findViewById(R.id.edAddAvtoNomer)
        avtoNomer.addTextChangedListener(EditSpaces(avtoNomer))
        binding.apply { edTechNomer.addTextChangedListener(EditHideKeyboard(edTechNomer)) }

    }

    private fun sendCarData(techSeriya: String, techNomer: String, avtoRegion: String, avtoNomer: String) {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.VISIBLE

                }
                val accessToken = sharedPreferences.getString("access_token", null)
                if (accessToken == null) {
                    withContext(Dispatchers.Main) {

                        Toast.makeText(requireContext(), "Токен недействителен", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }

                    return@launch
                }
                val formattedTechNomer = avtoNomer.replace(" ", "")
                val request = CheckCarRequest(
                    govNumber = "$avtoRegion$formattedTechNomer",
                    techPassportNumber = techNomer,
                    techPassportSeria = techSeriya
                )



                val response = apiService.checkCar("Bearer $accessToken", request)

                if (response.response.ERROR == "0") {
                    val carData = response.response
                    savedResponse = carData
                    withContext(Dispatchers.Main) {
                        binding.edOrgName.setText(carData.ORGNAME)
                        binding.edAvtoMark.setText(carData.MODEL_NAME)
                        binding.edAvtoYear.setText(carData.ISSUE_YEAR)

                        binding.edOrgName.isEnabled = false
                        binding.edAvtoMark.isEnabled = false
                        binding.edAvtoYear.isEnabled = false
                        binding.edAddAvtoNomer.isEnabled = false
                        binding.edAddAvtoRegion.isEnabled = false
                        binding.edTechNomer.isEnabled = false
                        binding.edTechSeriya.isEnabled =false

                        Toast.makeText(
                            requireContext(),
                            "Данные успешно получены",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.insideContainer.visibility = View.VISIBLE
                        binding.loadLayout.visibility = View.GONE
                        progressBar.visibility = View.GONE
                    }

                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Ошибка: ${response.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar.visibility = View.GONE
                    }

                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Ошибка сети", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Произошла ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        }
    }


    private fun sendCarDataToSecondAPI() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val accessToken = sharedPreferences.getString("access_token", null)
                if (accessToken == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Токен недействителен", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val response = savedResponse ?: run {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Нет данных для отправки", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val techNumber = binding.edTechNomer.text.toString().trim()
                val techSeriya = binding.edTechSeriya.text.toString().trim()
                val avtoRegion = binding.edAddAvtoRegion.text.toString().trim()
                val avtoNomer = binding.edAddAvtoNomer.text.toString().trim()
                val formattedTechNomer = avtoNomer.replace(" ", "")

                val addCarRequest = AddCarRequest(
                    BODY_NUMBER = response.BODY_NUMBER,
                    ENGINE_NUMBER = response.ENGINE_NUMBER,
                    FIRST_NAME = response.FIRST_NAME,
                    FY = response.FY,
                    GOV_NUMBER = "$avtoRegion$formattedTechNomer",
                    INN = response.INN,
                    ISSUE_YEAR = response.ISSUE_YEAR,
                    LAST_NAME = response.LAST_NAME,
                    MARKA_ID = response.MARKA_ID,
                    MIDDLE_NAME = response.MIDDLE_NAME,
                    MODEL_ID = response.MODEL_ID,
                    MODEL_NAME = response.MODEL_NAME,
                    ORGNAME = response.ORGNAME,
                    PINFL = response.PINFL,
                    TECH_NUMBER = techNumber,
                    TECH_PASSPORT_ISSUE_DATE = response.TECH_PASSPORT_ISSUE_DATE,
                    TECH_SERIYA = techSeriya,
                    USE_TERRITORY = response.USE_TERRITORY,
                    VEHICLE_TYPE_ID = response.VEHICLE_TYPE_ID
                )



                val addCarResponse = apiService.addCar("Bearer $accessToken", addCarRequest)

                withContext(Dispatchers.Main) {
                    if (addCarResponse.status == 200) {
                        Toast.makeText(requireContext(), "Данные успешно отправлены", Toast.LENGTH_SHORT).show()

                        dismiss()

                    } else {
                        Toast.makeText(requireContext(), "Ошибка: ${addCarResponse.message}", Toast.LENGTH_SHORT).show()

                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Ошибка сети", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Произошла ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                }

            } finally {
                CommonUtils.reloadFragment(requireActivity().supportFragmentManager, CabinetFragment())
            }
        }
    }

}








