package com.example.epolisplusapp.util

import android.widget.TextView

fun applyPhoneNumberMask(phoneNumber: String, mask: String): String {

    val prefix = "+"
    val strippedText = phoneNumber.removePrefix(prefix)
    val formattedText = formatNumber(mask, strippedText)
    return if (formattedText.isNotEmpty()) {
        prefix + formattedText
    } else {
        formattedText
    }
}

private fun formatNumber(mask: String, rawText: String): String {
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
