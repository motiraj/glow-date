@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.bs_addateconverteragecalculator.data.PreferencesManager
import com.example.bs_addateconverteragecalculator.widget.NepaliDateClockReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * BroadcastReceiver triggered at midnight (00:00:02) and upon system date/time/timezone changes.
 * Ensures that persistent lock screen notifications and all home screen/keyguard Glance widgets
 * immediately update to the new calendar day without requiring the user to open or restart the app.
 */
class MidnightUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. Re-arm the midnight alarm for the following midnight
                NotificationHelper.scheduleMidnightUpdateAlarm(context)

                // 2. Update persistent lock screen / status bar date notification if enabled
                val prefs = PreferencesManager(context)
                val isPersistentEnabled = try {
                    prefs.persistentNotificationFlow.first()
                } catch (_: Exception) {
                    true
                }
                if (isPersistentEnabled) {
                    NotificationHelper.showPersistentDateNotification(context)
                }

                // 3. Update all Glance widgets (DateClock, Panchanga, Rashifal, Calendar)
                NepaliDateClockReceiver.updateAllWidgetsSuspending(context)

                // 4. Reschedule daily alerts and festivals
                NotificationHelper.scheduleDailyNotification(context)
                FestivalNotificationHelper.scheduleFestivalAlarms(context)
                VedicTimingNotificationScheduler.scheduleDailyTimingAlarms(context)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        const val ACTION_MIDNIGHT_UPDATE = "com.example.bs_addateconverteragecalculator.ACTION_MIDNIGHT_UPDATE"
    }
}
