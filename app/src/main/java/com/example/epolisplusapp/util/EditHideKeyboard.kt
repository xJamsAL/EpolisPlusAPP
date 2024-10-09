package com.example.epolisplusapp.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class EditHideKeyboard(private val editText: EditText): TextWatcher {

    private val maxLength = 7
    private var isFormating = false
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {

        if (isFormating) return

        val input = s.toString().replace("\\s".toRegex(),"")

        if (input.length >= maxLength){
            hidekeyboar()
        }

    }
    private fun hidekeyboar(){
        val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}