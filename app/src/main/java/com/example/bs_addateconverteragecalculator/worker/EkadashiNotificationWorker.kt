@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class EkadashiNotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            FestivalNotificationHelper.checkAndNotifyFestival(applicationContext)
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
