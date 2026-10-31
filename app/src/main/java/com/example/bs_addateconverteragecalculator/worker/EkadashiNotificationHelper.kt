@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.worker

import android.content.Context

/**
 * Backward compatibility wrapper delegating to [FestivalNotificationHelper]
 * which handles all Nepali festivals, sacred parvas, and Ekadashis.
 */
object EkadashiNotificationHelper {

    const val CHANNEL_ID = FestivalNotificationHelper.CHANNEL_ID
    const val NOTIFICATION_ID = FestivalNotificationHelper.NOTIFICATION_ID

    fun createNotificationChannel(context: Context) {
        FestivalNotificationHelper.createNotificationChannel(context)
    }

    fun checkAndNotifyEkadashi(context: Context) {
        FestivalNotificationHelper.checkAndNotifyFestival(context)
    }

    fun scheduleEkadashiAlarms(context: Context) {
        FestivalNotificationHelper.scheduleFestivalAlarms(context)
    }
}
