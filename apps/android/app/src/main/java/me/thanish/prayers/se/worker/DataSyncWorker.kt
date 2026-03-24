package me.thanish.prayers.se.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import me.thanish.prayers.se.domain.PrayerTimeRepository
import me.thanish.prayers.se.worker.SyncNotificationHelper
import java.util.concurrent.TimeUnit

/**
 * Worker to sync prayer times from webapi
 * This worker runs once a day to check for updates
 */
class DataSyncWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            PrayerTimeRepository.syncIfNeeded(applicationContext) { current, total ->
                SyncNotificationHelper.showProgress(applicationContext, current, total)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sync failed", e)
            return Result.retry()
        } finally {
            SyncNotificationHelper.clear(applicationContext)
        }
        return Result.success()
    }

    companion object {
        private const val TAG = "SyncWorker"

        /**
         * Initialize the sync worker to run every 24 hours
         */
        fun initialize(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request =
                PeriodicWorkRequestBuilder<DataSyncWorker>(repeatInterval = 24, TimeUnit.HOURS)
                    .setConstraints(constraints)
                    .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                uniqueWorkName = TAG,
                existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP,
                request = request
            )
        }
    }
}
