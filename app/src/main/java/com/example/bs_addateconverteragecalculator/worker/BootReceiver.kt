@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.bs_addateconverteragecalculator.widget.NepaliDateClockReceiver

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == Intent.ACTION_BOOT_COMPLETED || 
            action == Intent.ACTION_MY_PACKAGE_REPLACED ||
            action == "android.intent.action.QUICKBOOT_POWERON") {
            try {
                // Initialize channels
                NotificationHelper.createNotificationChannel(context)
                FestivalNotificationHelper.createNotificationChannel(context)
                VedicTimingNotificationWorker.createNotificationChannels(context)

                // Schedule all alarms & workers
                NotificationHelper.scheduleMidnightUpdateAlarm(context)
                NotificationHelper.scheduleDailyNotification(context)
                NotificationScheduler.scheduleDailyVedicNotification(context)
                FestivalNotificationHelper.scheduleFestivalAlarms(context)
                VedicTimingNotificationScheduler.scheduleDailyTimingAlarms(context)

                // Restore persistent lock screen notification & refresh all widgets
                NotificationHelper.updatePersistentDateNotification(context)
                NepaliDateClockReceiver.updateAllWidgets(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
