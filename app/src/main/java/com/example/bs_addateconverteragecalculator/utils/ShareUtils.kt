package com.example.bs_addateconverteragecalculator.utils

import android.content.Context
import android.content.Intent

object ShareUtils {
    fun shareText(context: Context, text: String, title: String = "Share via") {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooser = Intent.createChooser(intent, title)
        context.startActivity(chooser)
    }
}
