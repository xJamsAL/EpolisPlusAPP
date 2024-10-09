package com.example.epolisplusapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epolisplusapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SosDialog:BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_sos_buttons, container, false)

    }
}