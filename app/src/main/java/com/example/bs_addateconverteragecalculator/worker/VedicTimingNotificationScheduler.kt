package com.example.bs_addateconverteragecalculator.worker

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.bs_addateconverteragecalculator.data.PanchangaCalculator
import com.example.bs_addateconverteragecalculator.data.PreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object VedicTimingNotificationScheduler {

    fun scheduleDailyTimingAlarms(context: Context) {
        try {
            val workManager = WorkManager.getInstance(context)
            val todayAd = LocalDate.now()
            val now = LocalDateTime.now()

            val prefs = PreferencesManager(context)
            val location = runBlocking { prefs.locationFlow.first() }

            val intervals = PanchangaCalculator.getMuhuratIntervals(todayAd, location.lat, location.lon, location.tzOffset)

            intervals.forEach { interval ->
                val startMins = interval.startMinutes
                // Trigger 10 minutes BEFORE start time
                val triggerMinsBefore = startMins - 10
                val triggerHour = triggerMinsBefore / 60
                val triggerMinute = triggerMinsBefore % 60

                val triggerDateTime = todayAd.atTime(triggerHour, triggerMinute)
                if (triggerDateTime.isAfter(now)) {
                    val delayMillis = Duration.between(now, triggerDateTime).toMillis()
                    val timingType = if (interval.title.contains("अभिजित")) "abhijit" else "rahu"
                    val workName = "vedic_timing_${timingType}_$todayAd"

                    val inputData = Data.Builder()
                        .putString(VedicTimingNotificationWorker.KEY_TIMING_TYPE, timingType)
                        .build()

                    val request = OneTimeWorkRequestBuilder<VedicTimingNotificationWorker>()
                        .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                        .setInputData(inputData)
                        .build()

                    workManager.enqueueUniqueWork(
                        workName,
                        androidx.work.ExistingWorkPolicy.REPLACE,
                        request
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
