@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EkadashiAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()

        // Reschedule festival alarms for upcoming days
        FestivalNotificationHelper.scheduleFestivalAlarms(context)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                FestivalNotificationHelper.checkAndNotifyFestival(context)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult.finish()
            }
        }
    }
}
