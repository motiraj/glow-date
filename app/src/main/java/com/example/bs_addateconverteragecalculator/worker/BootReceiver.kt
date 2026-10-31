@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == Intent.ACTION_BOOT_COMPLETED || 
            action == Intent.ACTION_MY_PACKAGE_REPLACED ||
            action == "android.intent.action.QUICKBOOT_POWERON") {
            NotificationScheduler.scheduleDailyVedicNotification(context)
            FestivalNotificationHelper.scheduleFestivalAlarms(context)
        }
    }
}
