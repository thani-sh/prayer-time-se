package me.thanish.prayers.se.worker

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import me.thanish.prayers.se.times.PrayerTime

/**
 * Worker to show a notification approximately 10 minutes before a prayer time
 * with a countdown timer to show the time remaining.
 */
class NotificationWorker : BroadcastReceiver() {
    /**
     * Runs approximately 10 minutes before the prayer time
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        val prayerTimeId = intent?.getStringExtra(INPUT_PRAYER_TIME_ID) ?: return
        doNotify(context!!, PrayerTime.deserialize(prayerTimeId))
    }

    /**
     * Create a timer notification for a specific prayer time
     */
    private fun doNotify(context: Context, prayerTime: PrayerTime) {
        Log.i(TAG, "Creating notification for prayer time: $prayerTime")
        val manager = context.getSystemService(NotificationManager::class.java)
        val notificationId = prayerTime.toEpochSeconds()
        val notificationBuilder = NotificationCompat.Builder(context, CH_ID)
            .setUsesChronometer(true)
            .setShowWhen(true)
            .setWhen(prayerTime.toEpochMilli())
            .setTimeoutAfter(1000 * 60)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Det är dags för ${prayerTime.name}")
            .setContentText("Det är dags för ${prayerTime.name}")

        manager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "NotificationWorker"
        private const val ACTION = "me.thanish.prayers.se.NOTIFY"
        private const val CH_ID = "prayer_time"
        private const val CH_NAME = "Prayer time"
        private const val CH_DESCRIPTION = "Notifies when it's time for prayers"
        private const val CH_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH
        private const val INPUT_PRAYER_TIME_ID = "prayerTimeId"
        private const val NOTIFICATION_DELAY_MS = 10 * 60 * 1000

        /**
         * Initialize the notification channel for prayer time notifications
         */
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun initialize(context: Context) {
            // Create the notification channel
            val manager = context.getSystemService(NotificationManager::class.java)
            val channel = NotificationChannel(CH_ID, CH_NAME, CH_IMPORTANCE).apply {
                description = CH_DESCRIPTION
            }
            manager.createNotificationChannel(channel)
            // Register the notification worker to receive broadcasts
            context.registerReceiver(
                NotificationWorker(),
                IntentFilter(ACTION),
                Context.RECEIVER_NOT_EXPORTED
            )
        }

        /**
         * Schedule a notification for a specific prayer time
         */
        fun schedule(context: Context, prayerTime: PrayerTime) {
            Log.i(TAG, "Scheduling notification for prayer time: $prayerTime")
            val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
            val alarmIntent = buildIntent(context, prayerTime)
            if (!AlarmManagerCompat.canScheduleExactAlarms(alarmManager)) {
                Log.w(TAG, "Cannot schedule exact alarms")
                return
            }
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                getNotificationTime(prayerTime),
                alarmIntent
            )
        }

        /**
         * Helper function to build a notification worker for a specific prayer time
         */
        private fun buildIntent(context: Context, prayerTime: PrayerTime): PendingIntent {
            val intent = Intent(context, NotificationWorker::class.java).apply {
                action = ACTION
                putExtra(INPUT_PRAYER_TIME_ID, prayerTime.serialize())
            }
            return PendingIntent.getBroadcast(
                context,
                prayerTime.toUniqueInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        /**
         * Helper function to get the timeout for the notification worker
         */
        private fun getNotificationTime(prayerTime: PrayerTime): Long {
            val timestamp = prayerTime.toEpochMilli() - NOTIFICATION_DELAY_MS
            if (timestamp < System.currentTimeMillis()) {
                return System.currentTimeMillis() + 1000 * 5
            }
            return timestamp
        }
    }
}
