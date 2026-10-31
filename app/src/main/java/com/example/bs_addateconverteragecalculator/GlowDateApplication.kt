@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager

class GlowDateApplication : Application(), Configuration.Provider {

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()
        try {
            WorkManager.initialize(this, workManagerConfiguration)
        } catch (e: Exception) {
            Log.e("GlowDateApp", "WorkManager initialization notice: ${e.message}")
        }
    }
}
