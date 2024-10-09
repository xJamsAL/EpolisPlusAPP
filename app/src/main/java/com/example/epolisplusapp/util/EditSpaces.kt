package com.example.epolisplusapp.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class EditSpaces(private val editText: EditText) : TextWatcher {
    private var isFormatting = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isFormatting) return

        isFormatting = true
        val input = s.toString().replace("\\s".toRegex(), "")
        val formatted = StringBuilder()
        val isFirstCharDigit = input.isNotEmpty() && input[0].isDigit()


        val avtoLenght = if (isFirstCharDigit) 7 else 7


        if (isFirstCharDigit) {

            for (i in input.indices) {
                if (i == 3) {
                    formatted.append(' ')
                }
                formatted.append(input[i])

                if (formatted.length >= avtoLenght) break
            }
        } else {

            for (i in input.indices) {
                if (i == 1 || i == 4) {
                    formatted.append(' ')
                }
                formatted.append(input[i])
                if (formatted.length > 8 ) break

            }

        }




        editText.setText(formatted.toString())
        editText.setSelection(formatted.length.coerceAtMost(avtoLenght))

        isFormatting = false
    }
}
