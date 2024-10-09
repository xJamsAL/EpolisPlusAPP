package com.example.epolisplusapp.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.epolisplusapp.R
import com.example.epolisplusapp.databinding.MainFragmentBinding
import com.example.epolisplusapp.models.profile.SharedViewModel
import com.example.epolisplusapp.ui.dopservice.DopUslugiActivity
import com.example.epolisplusapp.ui.settings.SettingsActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.button.MaterialButton

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var buttons: Array<MaterialButton>
    private lateinit var cardViews: Array<CardView>
    private lateinit var recyclerViews: Array<RecyclerView>
    private lateinit var nameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("UnsafeOptInUsageError")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = android.graphics.Color.TRANSPARENT
        }
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        nameTextView = view.findViewById(R.id.tvToolbarMain)
        sharedViewModel.textData.observe(viewLifecycleOwner){text->
            nameTextView.text = text
        }



        binding.apply {
            buttons = arrayOf(btSvKasko, btSvOsago, btSvTravel, btSvService, btSvGarantiya)
            recyclerViews = arrayOf(rcKasko, rcOsago, rcTravel, rcDopUslugi, rcGarant)
            cardViews = arrayOf(kasko, osago, travel, dopUslugi, garant)
        }
        showCardView(cardViews[0], buttons[0])
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                showCardView(cardViews[index], button)
                changeImageResource(index)
            }
        }
        binding.extraBt.setOnClickListener {
            val intent = Intent(activity, ExtraActivity::class.java)
            startActivity(intent)
        }
        binding.btDopUslugi.setOnClickListener {
            val intent = Intent(activity, DopUslugiActivity::class.java)
            startActivity(intent)
        }
        loadDataFromServer()

        val toolbar:MaterialToolbar = binding.tolbarMain
        val badgeDrawable: BadgeDrawable = BadgeDrawable.create(requireContext()).apply {


            backgroundColor = resources.getColor(R.color.red, null)
            isVisible = true
        }

        BadgeUtils.attachBadgeDrawable(badgeDrawable, toolbar, R.id.notification)

        binding.tolbarMain.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.shield ->{
                    Toast.makeText(context, "Shield clicked", Toast.LENGTH_SHORT).show()
                    true }
                R.id.notification ->{
                    Toast.makeText(context, "Notification clicked", Toast.LENGTH_SHORT).show()
                    true}
                R.id.setting ->{
                    val intent = Intent(activity, SettingsActivity::class.java)
                    startActivity(intent)
                    true}
                else -> false
            }
        }

    }






    private fun toggleViews(index: Int) {
        recyclerViews.forEach { it.visibility = View.GONE }
        cardViews.forEach { it.visibility = View.GONE }

        if (dataIsAvailable(index)) {
            recyclerViews[index].visibility = View.VISIBLE
        } else {
            cardViews[index].visibility = View.VISIBLE
        }
    }

    private fun dataIsAvailable(index: Int): Boolean {
        // Логика проверки наличия данных для RecyclerView
        return true
    }

    private fun loadDataFromServer() {
        // Логика загрузки данных с сервера
        // После получения данных вызовите toggleViews(index) для обновления UI
    }

    private fun showCardView(activeCardView: CardView, activeButton: MaterialButton) {
        cardViews.forEach { it.visibility = View.GONE }
        activeCardView.visibility = View.VISIBLE
        setActiveButton(activeButton)
    }

    private fun setActiveButton(activeButton: MaterialButton) {
        val defaultColor = ContextCompat.getColorStateList(requireContext(), R.color.not_color)
        val activeColor = ContextCompat.getColorStateList(requireContext(), R.color.white)
        val textColorActive = ContextCompat.getColorStateList(requireContext(), R.color.black)
        val iconColorDefault = ContextCompat.getColorStateList(requireContext(), R.color.white)
        val iconColors = arrayOf(
            ContextCompat.getColorStateList(requireContext(), R.color.green),
            ContextCompat.getColorStateList(requireContext(), R.color.blue),
            ContextCompat.getColorStateList(requireContext(), R.color.red),
            ContextCompat.getColorStateList(requireContext(), R.color.green),
            ContextCompat.getColorStateList(requireContext(), R.color.green)
        )
        buttons.forEachIndexed { _, button ->
            button.backgroundTintList = defaultColor
            button.setTextColor(activeColor!!)
            button.iconTint = iconColorDefault
        }

        activeButton.backgroundTintList = activeColor
        activeButton.setTextColor(textColorActive!!)

        val activeIndex = buttons.indexOf(activeButton)
        activeButton.iconTint = iconColors[activeIndex]
    }

    private fun changeImageResource(index: Int) {
        val imageResId = when (index) {
            1 -> R.drawable.main_back_osago
            2 -> R.drawable.main_back_travel
            else -> R.drawable.main_back_kasco
        }
        binding.mainBack.setImageResource(imageResId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
