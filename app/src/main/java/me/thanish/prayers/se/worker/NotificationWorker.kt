package me.thanish.prayers.se.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.thanish.prayers.se.times.PrayerTime
import java.util.concurrent.TimeUnit

/**
 * Worker to show a notification approximately 10 minutes before a prayer time
 * with a countdown timer to show the time remaining.
 */
class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    /**
     * Runs approximately 10 minutes before the prayer time
     */
    override fun doWork(): Result {
        try {
            val prayerTimeId = inputData.getString(INPUT_PRAYER_TIME_ID) ?: return Result.failure()
            doNotify(applicationContext, PrayerTime.deserialize(prayerTimeId))
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }
        return Result.success()
    }

    companion object {
        private const val CH_ID = "almost_prayer_time"
        private const val CH_NAME = "Almost prayer time"
        private const val CH_DESCRIPTION = "Notifies when it's almost time for a prayer time"
        private const val CH_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT

        // Keys for input data used by the worked
        private const val INPUT_PRAYER_TIME_ID = "prayerTimeId"
        private const val NOTIFICATION_DELAY_MS = 10 * 60 * 1000

        /**
         * Initialize the notification channel for prayer time notifications
         */
        fun initialize(context: Context) {
            val manager = context.getSystemService(NotificationManager::class.java)
            val channel = NotificationChannel(CH_ID, CH_NAME, CH_IMPORTANCE).apply {
                description = CH_DESCRIPTION
            }
            manager.createNotificationChannel(channel)
        }

        /**
         * Create a timer notification for a specific prayer time
         */
        fun doNotify(context: Context, prayerTime: PrayerTime) {
            println("creating notification for prayer time: $prayerTime")
            val manager = context.getSystemService(NotificationManager::class.java)
            val timeout = prayerTime.toEpochMilli()
            val notificationId = prayerTime.toEpochSeconds()
            val notificationBuilder = NotificationCompat.Builder(context, CH_ID)
                .setUsesChronometer(true)
                .setShowWhen(true)
                .setWhen(timeout)
                .setTimeoutAfter(timeout + 1000 * 60 * 10) // 10 minutes)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Det är dags för ${prayerTime.name}")
                .setContentText("Det är dags för ${prayerTime.name}")

            manager.notify(notificationId, notificationBuilder.build())
        }

        /**
         * Helper function to build a notification worker for a specific prayer time
         */
        fun buildRequest(prayerTime: PrayerTime): OneTimeWorkRequest {
            val timeout = getNotificationTimeout(prayerTime)
            val inputs = getNotificationInputData(prayerTime)
            return OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(timeout, TimeUnit.MILLISECONDS)
                .setInputData(inputs)
                .build()
        }

        /**
         * Helper function to get the name of the notification worker
         */
        fun getNotificationWorkerName(prayerTime: PrayerTime): String {
            return prayerTime.serialize()
        }

        /**
         * Helper function to get the timeout for the notification worker
         */
        private fun getNotificationTimeout(prayerTime: PrayerTime): Long {
            return prayerTime.millisUntil() - NOTIFICATION_DELAY_MS
        }

        /**
         * Helper function to build input data for the notification worker
         */
        private fun getNotificationInputData(prayerTime: PrayerTime): Data {
            return Data.Builder()
                .putString(INPUT_PRAYER_TIME_ID, prayerTime.serialize())
                .build()
        }
    }
}
