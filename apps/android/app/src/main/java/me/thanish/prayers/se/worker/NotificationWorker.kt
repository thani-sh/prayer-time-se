package me.thanish.prayers.se.worker

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import me.thanish.prayers.se.R
import me.thanish.prayers.se.domain.NotificationOffset
import me.thanish.prayers.se.domain.PrayerTime
import me.thanish.prayers.se.domain.PrayerTimeCity
import me.thanish.prayers.se.domain.PrayerTimeType
import java.time.LocalDateTime

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
        val prayerTime = PrayerTime.fromStringId(prayerTimeId) ?: return
        doNotify(context!!, prayerTime)
    }

    /**
     * Create a timer notification for a specific prayer time
     */
    private fun doNotify(context: Context, prayerTime: PrayerTime) {
        if (!NotificationOffset.isEnabled(context)) {
            Log.i(TAG, "Notifications are disabled")
            return
        }
        if (PrayerTimeCity.get(context) != prayerTime.city) {
            Log.i(TAG, "Notifications are for a different city")
            return
        }
        Log.i(TAG, "Creating notification for prayer time: $prayerTime")
        val manager = context.getSystemService(NotificationManager::class.java)
        val notificationId = prayerTime.getEpochSeconds()
        val notificationExpiresIn = getNotificationExpireTime(prayerTime)
        if (notificationExpiresIn <= 0.toLong()) {
            Log.i(TAG, "Notification already expired. Ignoring it.")
            return
        }
        val notificationBuilder = NotificationCompat.Builder(context, CH_ID)
            .setUsesChronometer(true)
            .setShowWhen(true)
            .setWhen(prayerTime.getEpochMilli())
            .setTimeoutAfter(notificationExpiresIn)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(context.getString(R.string.notification_title, prayerTime.type.getLabel(context)))
            .setContentText(context.getString(R.string.notification_title, prayerTime.type.getLabel(context)))

        manager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "NotificationWorker"
        private const val ACTION = "me.thanish.se.prayers.NOTIFY"
        private const val CH_ID = "prayer_time"
        private const val INPUT_PRAYER_TIME_ID = "prayerTimeId"


        /**
         * Initialize the notification channel for prayer time notifications.
         */
        fun initialize(context: Context) {
            // Create the notification channel
            val manager = context.getSystemService(NotificationManager::class.java)
            val channelName = context.getString(R.string.notification_channel_title)
            val channelPrio = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CH_ID, channelName, channelPrio).apply {
                description = context.getString(R.string.notification_channel_description)
            }
            // Create the notification channel
            manager.createNotificationChannel(channel)
        }

        /**
         * Schedule a notification for a specific prayer time.
         */
        fun schedule(context: Context, prayerTime: PrayerTime) {
            Log.i(TAG, "Scheduling notification for prayer time: $prayerTime")
            val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
            val alarmIntent = buildIntent(context, prayerTime)
            if (!AlarmManagerCompat.canScheduleExactAlarms(alarmManager)) {
                Log.w(TAG, "Cannot schedule exact alarms")
                return
            }
            AlarmManagerCompat.setAlarmClock(
                alarmManager,
                getNotificationTime(context, prayerTime),
                alarmIntent,
                alarmIntent
            )
        }

        /**
         * Schedule a test notification for a testing prayer time.
         * Only used for testing scheduling notifications.
         */
        fun scheduleTestNotification(context: Context, delay: Long) {
            Log.i(TAG, "Scheduling test notification with a $delay seconds delay")
            val testPrayerTime = PrayerTime(
                city = PrayerTimeCity.get(context),
                type = PrayerTimeType.asr,
                time = LocalDateTime.now().plusSeconds(delay)
            )
            schedule(context, testPrayerTime)
        }

        /**
         * Helper function to build a notification worker for a specific prayer time.
         */
        private fun buildIntent(context: Context, prayerTime: PrayerTime): PendingIntent {
            val intent = Intent(context, NotificationWorker::class.java).apply {
                action = ACTION
                putExtra(INPUT_PRAYER_TIME_ID, prayerTime.getStringId())
            }
            return PendingIntent.getBroadcast(
                context,
                prayerTime.getIntId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        /**
         * Helper function to get the timeout for the notification worker.
         */
        private fun getNotificationTime(context: Context, prayerTime: PrayerTime): Long {
            val timestamp = prayerTime.getEpochMilli() - NotificationOffset.get(context).getMilli()
            if (timestamp < System.currentTimeMillis()) {
                return System.currentTimeMillis() + 1000 * 5
            }
            return timestamp
        }

        /**
         * Helper function to get the expire time for the notification worker.
         * Returns the duration until the notification expires in milliseconds.
         */
        private fun getNotificationExpireTime(prayerTime: PrayerTime): Long {
            val prayerTimestamp = prayerTime.getEpochMilli()
            val currentTimestamp = System.currentTimeMillis()
            if (prayerTimestamp <= currentTimestamp) {
                return 0
            }
            return prayerTimestamp - currentTimestamp
        }
    }
}
