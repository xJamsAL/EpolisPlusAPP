import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import com.example.epolisplusapp.R
import com.example.epolisplusapp.api.MainApi
import com.example.epolisplusapp.models.cabinet.request.UpdateProfileRequest
import com.example.epolisplusapp.models.profile.SharedViewModel
import com.example.epolisplusapp.service.RetrofitInstance
import com.example.epolisplusapp.ui.cabinet.CabinetFragment
import com.example.epolisplusapp.ui.cabinet.DeleteDialog
import com.example.epolisplusapp.util.CommonUtils
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import eightbitlab.com.blurview.BlurView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PersonalDialog : BottomSheetDialogFragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var personalBlur: BlurView
    private lateinit var apiService: MainApi.ApiService
    private lateinit var nameEditText: EditText
    private lateinit var progressBar: BlurView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cabinet_dialog_personal, container, false)
    }

    @SuppressLint("CommitTransaction")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbarPersonalCabinet)
        personalBlur = view.findViewById(R.id.personalBlur)
        progressBar = view.findViewById(R.id.progBarPerDialog)
        CommonUtils.setupToolbarDialog(toolbar, this)
        CommonUtils.setupBlurViewForFragment(this, progressBar)
        CommonUtils.setupBlurViewForFragment(this, personalBlur)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        nameEditText = view.findViewById(R.id.edCabinetDialog)

        sharedViewModel.textData.observe(viewLifecycleOwner) { text ->
            nameEditText.setText(text)
        }

        sharedViewModel.showProgressBar.observe(viewLifecycleOwner) { show ->
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
        }

        apiService = RetrofitInstance(requireContext()).api
        sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        CommonUtils.setupBottomSheetBehavior(view, this)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val deleteFragment: FragmentContainerView = view.findViewById(R.id.deleteFragment)
        val btDeleteAccount: Button = view.findViewById(R.id.btDeleteAccount)
        btDeleteAccount.setOnClickListener {
            if (deleteFragment.visibility == View.GONE){
                personalBlur.visibility = View.VISIBLE
                deleteFragment.visibility = View.VISIBLE
                childFragmentManager.beginTransaction()
                    .replace(R.id.deleteFragment, DeleteDialog())
                    .addToBackStack(null)
                    .commit()
            }


        }

        val btSaveDelete:Button = view.findViewById(R.id.btSaveUpdate)
        btSaveDelete.setOnClickListener {
            val fullName = nameEditText.text.toString()
            if (fullName.isNotEmpty()){
                updateProfile(fullName)
            }
            else{
                Toast.makeText(requireContext(), "Поле имени не может быть пустым", Toast.LENGTH_SHORT).show()
            }
        }



    }
    fun showProgressBar(show: Boolean) {
        sharedViewModel.setShowProgressBar(show)
    }


    private fun updateProfile(nameEditText: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main){

                }
                val accessToken = sharedPreferences.getString("access_token", null)
                if (accessToken == null) {
                    withContext(Dispatchers.Main) {

                        Toast.makeText(requireContext(), "Токен недействителен", Toast.LENGTH_SHORT).show()
                    }

                    return@launch
                }
                val request = UpdateProfileRequest(full_name =nameEditText)

                val response = apiService.updateProfile("Bearer $accessToken", request)
                if (response.status == 200){
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), "Профиль успешно обновлен", Toast.LENGTH_SHORT).show()
                    dismiss()
                    }
                }
                else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Ошибка при обновлении профиля", Toast.LENGTH_SHORT).show()
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
            }finally {
                CommonUtils.reloadFragment(requireActivity().supportFragmentManager, CabinetFragment())
            }
        }

    }


}

