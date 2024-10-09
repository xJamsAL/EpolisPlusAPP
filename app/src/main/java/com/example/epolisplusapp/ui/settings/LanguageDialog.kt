package com.example.epolisplusapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epolisplusapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LanguageDialog: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.setting_language_dialog, container, false)
    }




}