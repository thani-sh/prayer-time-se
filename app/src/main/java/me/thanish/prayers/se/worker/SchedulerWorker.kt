package me.thanish.prayers.se.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.thanish.prayers.se.domain.NotificationOffset
import me.thanish.prayers.se.domain.PrayerTime
import me.thanish.prayers.se.domain.PrayerTimeCity
import java.util.concurrent.TimeUnit

/**
 * Worker to schedule notifications for prayer times
 * This worker runs every hour to schedule notifications for the next N prayers
 */
class SchedulerWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    /**
     * Runs approximately every hour to schedule notifications
     */
    override fun doWork(): Result {
        try {
            schedule(applicationContext, PrayerTimeCity.get())
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }
        return Result.success()
    }

    companion object {
        private const val TAG = "SchedulerWorker"
        private const val PRAYERS_TO_SCHEDULE = 10

        /**
         * Initialize the scheduler worker to run every hour
         */
        fun initialize(context: Context) {
            val request =
                PeriodicWorkRequestBuilder<SchedulerWorker>(repeatInterval = 1, TimeUnit.HOURS)
                    .setConstraints(Constraints(requiresDeviceIdle = true))
                    .build()
            // Cancel and re-enqueue the work if it already exists
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                uniqueWorkName = TAG,
                existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                request = request
            )
            // Immediately schedule notifications for next N prayers
            schedule(context, PrayerTimeCity.get())
        }

        /**
         * Schedule or reschedule notifications for next N prayer times
         */
        fun schedule(context: Context, city: PrayerTimeCity) {
            if (!NotificationOffset.isEnabled()) {
                Log.i(TAG, "Notifications are disabled")
                return
            }
            Log.i(TAG, "Scheduling notifications for next $PRAYERS_TO_SCHEDULE prayers")
            PrayerTime.getNext(context, city, PRAYERS_TO_SCHEDULE).forEach { t ->
                NotificationWorker.schedule(context, t)
            }
        }
    }
}
