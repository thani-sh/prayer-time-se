package me.thanish.prayers.se.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.thanish.prayers.se.states.Preferences
import me.thanish.prayers.se.times.getNextPrayerTimes
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
            schedule(applicationContext, Preferences.getCity())
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
                uniqueWorkName = getNotificationWorkerName(),
                existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                request = request
            )
            // Immediately schedule notifications for next N prayers
            schedule(context, Preferences.getCity())
        }

        /**
         * Schedule or reschedule notifications for next N prayer times
         */
        fun schedule(context: Context, city: String) {
            if (!Preferences.getNotifyEnabled()) {
                Log.i(TAG, "Notifications are disabled")
                return
            }
            Log.i(TAG, "Scheduling notifications for next $PRAYERS_TO_SCHEDULE prayers")
            getNextPrayerTimes(context, city, PRAYERS_TO_SCHEDULE).forEach { t ->
                NotificationWorker.schedule(context, t)
            }
        }

        /**
         * Helper function to get the name of the notification worker
         */
        private fun getNotificationWorkerName(): String {
            return "SchedulerWorker"
        }
    }
}
