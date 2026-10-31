@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object NotificationScheduler {
    private const val WORK_NAME = "glow_date_daily_vedic_notification_work"

    fun scheduleDailyVedicNotification(context: Context) {
        val currentTime = Calendar.getInstance()
        val targetCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 5)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Randomize between 5:00 AM (0 mins) and 6:30 AM (90 mins)
        val randomMinutesOffset = Random.nextInt(0, 91)
        targetCalendar.add(Calendar.MINUTE, randomMinutesOffset)

        // If target time has already passed today, schedule for tomorrow morning
        if (targetCalendar.timeInMillis <= currentTime.timeInMillis) {
            targetCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val initialDelayMillis = targetCalendar.timeInMillis - currentTime.timeInMillis

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyVedicNotificationWorker>(
            24, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
            .build()

        try {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                dailyWorkRequest
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
