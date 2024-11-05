package com.example.epolisplusapp.ui.cabinet

import PersonalDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import com.example.epolisplusapp.R
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.models.cabinet.request.DeleteProfileRequest
import com.example.epolisplusapp.models.profile.SharedViewModel
import com.example.epolisplusapp.service.RetrofitInstance
import com.example.epolisplusapp.ui.auth.AuthMain
import eightbitlab.com.blurview.BlurView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DeleteDialog : Fragment() {
    private lateinit var apiService: MainApi.ApiService
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cabinet_dialog_delete_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btCancel: Button = view.findViewById(R.id.btCancel)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        apiService = RetrofitInstance(requireContext()).api
        sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        btCancel.setOnClickListener {
            (parentFragment as? PersonalDialog)?.view?.findViewById<FragmentContainerView>(R.id.deleteFragment)?.visibility = View.GONE
            (parentFragment as? PersonalDialog)?.view?.findViewById<BlurView>(R.id.personalBlur)?.visibility = View.GONE
        }
        val btDelete:Button = view.findViewById(R.id.btDelete)
        btDelete.setOnClickListener {
            deleteProfile()
        }



    }

    private fun deleteProfile() {
        (parentFragment as? PersonalDialog)?.showProgressBar(true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val phoneNumber = sharedViewModel.phoneData.value

                if (phoneNumber.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Номер телефона не найден", Toast.LENGTH_SHORT).show()
                        (parentFragment as? PersonalDialog)?.showProgressBar(false)
                    }
                    return@launch
                }

                val accessToken = sharedPreferences.getString("access_token", null)
                if (accessToken == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Токен недействителен", Toast.LENGTH_SHORT).show()
                        (parentFragment as? PersonalDialog)?.showProgressBar(false)
                    }
                    return@launch
                }

                val request = DeleteProfileRequest(phone = phoneNumber)

                val response = apiService.deleteProfile("Bearer $accessToken", request)

                if (response.status == 200 ) {
                    withContext(Dispatchers.Main) {
                        clearUserData()
                        navigateToAuthScreen()
                        Toast.makeText(requireContext(), "Профиль успешно удален", Toast.LENGTH_SHORT).show()
                        (parentFragment as? PersonalDialog)?.showProgressBar(false)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Ошибка при удалении профиля: ${response.message}", Toast.LENGTH_SHORT).show()
                        (parentFragment as? PersonalDialog)?.showProgressBar(false)
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Ошибка сети", Toast.LENGTH_SHORT).show()
                    (parentFragment as? PersonalDialog)?.showProgressBar(false)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Произошла ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                    (parentFragment as? PersonalDialog)?.showProgressBar(false)
                }
            }
        }
    }

    private fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }
    private fun navigateToAuthScreen() {
        val intent = Intent(requireContext(), AuthMain::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}
