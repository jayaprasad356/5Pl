package com.app.fivepl.helper

import android.text.TextWatcher
import android.widget.EditText

class DecimalFormatTextWatcher(etAmount: EditText) : TextWatcher {

    private val editText: EditText = etAmount
    private var current = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: android.text.Editable?) {
        if (s.toString() != current) {
            editText.removeTextChangedListener(this)
            val cleanString = s.toString().replace("[,.]".toRegex(), "")
            val parsed = cleanString.toDouble()
            val formatted = String.format("%,.0f", parsed)
            current = formatted
            editText.setText(formatted)
            editText.setSelection(formatted.length)
            editText.addTextChangedListener(this)
        }
    }


}
