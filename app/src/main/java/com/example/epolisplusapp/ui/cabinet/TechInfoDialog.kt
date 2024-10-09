package com.example.epolisplusapp.ui.cabinet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.epolisplusapp.R
import com.example.epolisplusapp.util.CommonUtils
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TechInfoDialog:BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cabinet_dialog_tech_nomer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar:MaterialToolbar = view.findViewById(R.id.materialToolbarTechPas)
        super.onViewCreated(view, savedInstanceState)
        CommonUtils.setupBottomSheetBehavior(view, this)
        CommonUtils.setupToolbarDialog(toolbar, this)
    }

}