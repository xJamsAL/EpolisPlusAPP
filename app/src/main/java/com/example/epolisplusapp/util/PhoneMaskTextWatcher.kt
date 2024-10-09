package com.example.epolisplusapp.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class PhoneNumberMaskWatcher(
    private val mask: String,
    private val editText: EditText
) : TextWatcher {
    private var isFormatting = false
    private val prefix: String = "+998 "

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(editable: Editable?) {
        if (isFormatting) return

        isFormatting = true

        val currentText = editable?.toString() ?: ""
        val strippedText = currentText.removePrefix(prefix).replace(Regex("\\D"), "")

        val formattedText = formatNumber(strippedText)
        val newText = if (formattedText.isNotEmpty()) {
            prefix + formattedText
        } else {
            formattedText
        }

        editText.setText(newText)
        editText.setSelection(newText.length)

        isFormatting = false
    }

    private fun formatNumber(rawText: String): String {
        val sb = StringBuilder()
        var rawIndex = 0
        for (maskChar in mask) {
            if (rawIndex >= rawText.length) break

            if (maskChar == '#') {
                sb.append(rawText[rawIndex])
                rawIndex++
            } else {
                sb.append(maskChar)
            }
        }
        return sb.toString()
    }
}
