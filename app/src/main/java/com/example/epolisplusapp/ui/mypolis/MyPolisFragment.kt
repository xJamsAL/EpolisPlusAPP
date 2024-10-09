package com.example.epolisplusapp.ui.mypolis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.epolisplusapp.R
import com.example.epolisplusapp.adapters.PartnersAdapter
import com.example.epolisplusapp.models.partners.Response
import com.google.android.material.button.MaterialButton

class MyPolisFragment : Fragment() {
    private lateinit var buttons: Array<MaterialButton>
    private lateinit var recyclerViews: Array<RecyclerView>
    private lateinit var imageView: ImageView
    private lateinit var partnersAdapter: PartnersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_polis_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = android.graphics.Color.TRANSPARENT
        }

        val testRecycle: RecyclerView = view.findViewById(R.id.firstRC)
        testRecycle.layoutManager = LinearLayoutManager(context)



        val testList = listOf(
         Response(1, "asdsa" , "sadsadadsad", "sadsadsadsa"),
            Response(1, "asdsa" , "sadsadadsad", "sadsadsadsa"),
            Response(1, "asdsa" , "sadsadadsad", "sadsadsadsa"),
            Response(1, "asdsa" , "sadsadadsad", "sadsadsadsa"),
            Response(1, "asdsa" , "sadsadadsad", "sadsadsadsa"),
            Response(1, "asdsa" , "sadsadadsad", "sadsadsadsa")

            )

        partnersAdapter = PartnersAdapter(testList)
        testRecycle.adapter =partnersAdapter

        imageView = view.findViewById(R.id.my_polis_back)

        buttons = arrayOf(
            view.findViewById(R.id.button11),
            view.findViewById(R.id.button12),
            view.findViewById(R.id.button13)
        )

        recyclerViews = arrayOf(
            view.findViewById(R.id.firstRC),
            view.findViewById(R.id.sendRC),
            view.findViewById(R.id.thirdRC)
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                changeImageResource(
                    when (index) {
                        0 -> R.drawable.my_polis_back_deystv
                        1 -> R.drawable.my_polis_back_oformlen
                        else -> R.drawable.my_polis_back_arxiv
                    }
                )
                showRecyclerView(recyclerViews[index], button)
            }
        }
        showRecyclerView(recyclerViews[0], buttons[0])
    }

    private fun showRecyclerView(activeRecyclerView: RecyclerView, activeButton: MaterialButton) {
        recyclerViews.forEach { it.visibility = View.GONE }
        activeRecyclerView.visibility = View.VISIBLE
        setActiveButton(activeButton)
    }

    private fun setActiveButton(activeButton: MaterialButton) {
        val defaultColor = ContextCompat.getColorStateList(requireContext(), R.color.not_color)
        val activeColor = ContextCompat.getColorStateList(requireContext(), R.color.white)
        val textColorActive = ContextCompat.getColorStateList(requireContext(), R.color.black)
        val iconColors = arrayOf(
            ContextCompat.getColorStateList(requireContext(), R.color.green),
            ContextCompat.getColorStateList(requireContext(), R.color.blue),
            ContextCompat.getColorStateList(requireContext(), R.color.grey)
        )

        buttons.forEach { button ->
            button.backgroundTintList = defaultColor
            button.setTextColor(activeColor!!)
            button.iconTint = activeColor
        }

        activeButton.backgroundTintList = activeColor
        activeButton.setTextColor(textColorActive)

        val activeIndex = buttons.indexOf(activeButton)
        activeButton.iconTint = iconColors[activeIndex]
    }

    private fun changeImageResource(imageResId: Int) {
        imageView.setImageResource(imageResId)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyPolisFragment()
    }
}
