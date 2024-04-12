package com.app.fivepl.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import com.app.fivepl.R

object DialogUtils {
    fun showCustomDialog(context: Context, message: String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.toast_custom_dialog)
        val dialogText = dialog.findViewById<TextView>(R.id.textView_dialog_message)
        val  button_dialog_ok = dialog.findViewById<TextView>(R.id.button_dialog_ok)
        dialogText.text = message
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        button_dialog_ok.setOnClickListener {
            dialog.dismiss()
        }
    }
}
