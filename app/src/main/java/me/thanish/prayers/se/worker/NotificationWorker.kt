package me.thanish.prayers.se.worker

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import androidx.work.WorkerParameters
import me.thanish.prayers.se.times.PrayerTime

/**
 * Worker to show a notification approximately 10 minutes before a prayer time
 * with a countdown timer to show the time remaining.
 */
class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    BroadcastReceiver() {

    /**
     * Runs approximately 10 minutes before the prayer time
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        val prayerTimeId = intent?.getStringExtra(INPUT_PRAYER_TIME_ID) ?: return
        doNotify(context!!, PrayerTime.deserialize(prayerTimeId))
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

        fun schedule(context: Context, prayerTime: PrayerTime) {
            println("scheduling notification for prayer time: $prayerTime")
            val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
            val alarmIntent = buildIntent(context, prayerTime)

            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                prayerTime.toEpochMilli(),
                alarmIntent
            )
        }

        /**
         * Create a timer notification for a specific prayer time
         */
        private fun doNotify(context: Context, prayerTime: PrayerTime) {
            println("creating notification for prayer time: $prayerTime")
            val manager = context.getSystemService(NotificationManager::class.java)
            val timeout = prayerTime.toEpochMilli()
            val notificationId = prayerTime.toEpochSeconds()
            val notificationBuilder = NotificationCompat.Builder(context, CH_ID)
                .setUsesChronometer(true)
                .setShowWhen(true)
                .setWhen(timeout)
                .setTimeoutAfter(timeout + 1000 * 60 * 5) // 5 minutes
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Det är dags för ${prayerTime.name}")
                .setContentText("Det är dags för ${prayerTime.name}")

            manager.notify(notificationId, notificationBuilder.build())
        }

        /**
         * Helper function to build a notification worker for a specific prayer time
         */
        private fun buildIntent(context: Context, prayerTime: PrayerTime): PendingIntent {
            val intent = Intent(context, NotificationWorker::class.java).apply {
                putExtra(INPUT_PRAYER_TIME_ID, prayerTime.serialize())
            }
            return PendingIntent.getBroadcast(
                context,
                prayerTime.toEpochSeconds(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        /**
         * Helper function to get the timeout for the notification worker
         */
        private fun getNotificationTime(prayerTime: PrayerTime): Long {
            return prayerTime.toEpochMilli() - NOTIFICATION_DELAY_MS
        }
    }
}
