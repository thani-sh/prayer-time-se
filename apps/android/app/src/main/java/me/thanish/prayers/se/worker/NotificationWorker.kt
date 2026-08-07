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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.thanish.prayers.se.R
import me.thanish.prayers.se.domain.NotificationOffset
import me.thanish.prayers.se.domain.PrayerTime
import me.thanish.prayers.se.domain.PrayerTimeCity
import me.thanish.prayers.se.domain.PrayerTimeMethod
import me.thanish.prayers.se.domain.PrayerTimeType
import me.thanish.prayers.se.widget.nextprayer.hasActiveWidgets
import me.thanish.prayers.se.widget.nextprayer.updateAllWidgets
import java.time.LocalDateTime

/**
 * Worker to show prayer notifications and auto-update home screen widgets.
 */
class NotificationWorker : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        if (intent.action == ACTION_MINUTE_TICK) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if (hasActiveWidgets(context)) {
                        updateAllWidgets(context)
                        scheduleMinuteTick(context)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error in minute tick execution", e)
                } finally {
                    pendingResult.finish()
                }
            }
            return
        }

        val prayerTimeId = intent.getStringExtra(INPUT_PRAYER_TIME_ID) ?: return
        val prayerTime = PrayerTime.fromStringId(prayerTimeId) ?: return
        val isExact = intent.getBooleanExtra(INPUT_IS_EXACT, false)

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Always update all widgets on any alarm trigger
                updateAllWidgets(context)

                // Show notification if enabled and city matches
                doNotify(context, prayerTime, isExact)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun doNotify(context: Context, prayerTime: PrayerTime, isExact: Boolean) {
        val manager = context.getSystemService(NotificationManager::class.java) ?: return

        // If sunrise is reached, cancel any active prayer notification (e.g. Fajr)
        if (prayerTime.type == PrayerTimeType.shuruk) {
            Log.i(TAG, "Sunrise reached ($prayerTime), cancelling active prayer notification")
            manager.cancel(PRAYER_NOTIFICATION_ID)
            return
        }

        if (!NotificationOffset.isEnabled(context)) {
            Log.i(TAG, "Notifications are disabled")
            return
        }
        if (PrayerTimeCity.get(context) != prayerTime.city) {
            Log.i(TAG, "Notifications are for a different city")
            return
        }

        Log.i(TAG, "Creating notification (isExact=$isExact) for prayer time: $prayerTime")
        val notificationExpiresIn = getNotificationExpireTime(prayerTime)

        if (!isExact && notificationExpiresIn <= 0L) {
            Log.i(TAG, "Notification already expired. Ignoring it.")
            return
        }

        val contentText = if (isExact) {
            context.getString(R.string.notification_on_time_body, prayerTime.type.getLabel(context))
        } else {
            context.getString(R.string.notification_body, prayerTime.type.getLabel(context), prayerTime.getTimeString(context))
        }

        val builder = NotificationCompat.Builder(context, CH_ID)
            .setShowWhen(true)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(context.getString(R.string.notification_title, prayerTime.type.getLabel(context)))
            .setContentText(contentText)

        if (!isExact) {
            builder.setUsesChronometer(true)
                .setWhen(prayerTime.getEpochMilli())
                .setTimeoutAfter(notificationExpiresIn)
        }

        // Using constant PRAYER_NOTIFICATION_ID ensures new notifications (e.g. Maghrib) replace old ones (e.g. Asr)
        manager.notify(PRAYER_NOTIFICATION_ID, builder.build())
    }

    companion object {
        private const val TAG = "NotificationWorker"
        private const val ACTION = "me.thanish.prayers.se.NOTIFY"
        private const val ACTION_MINUTE_TICK = "me.thanish.prayers.se.MINUTE_TICK"
        private const val CH_ID = "prayer_time"
        private const val PRAYER_NOTIFICATION_ID = 1001
        private const val INPUT_PRAYER_TIME_ID = "prayerTimeId"
        private const val INPUT_IS_EXACT = "isExact"
        private const val MINUTE_TICK_REQUEST_CODE = 99999

        fun initialize(context: Context) {
            val manager = context.getSystemService(NotificationManager::class.java)
            val channelName = context.getString(R.string.notification_channel_title)
            val channelPrio = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CH_ID, channelName, channelPrio).apply {
                description = context.getString(R.string.notification_channel_description)
            }
            manager.createNotificationChannel(channel)

            // Start minute-by-minute widget update loop
            scheduleMinuteTick(context)
        }

        /**
         * Schedule a ticker alarm for the top of the next minute to update widgets.
         */
        fun scheduleMinuteTick(context: Context) {
            val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
            val nextMinute = ((System.currentTimeMillis() / 60000) + 1) * 60000
            val intent = Intent(context, NotificationWorker::class.java).apply {
                action = ACTION_MINUTE_TICK
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                MINUTE_TICK_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            try {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC,
                    nextMinute,
                    pendingIntent
                )
            } catch (e: Exception) {
                Log.e(TAG, "Failed to schedule minute tick alarm", e)
            }
        }

        /**
         * Schedule exact alarm ON prayer time (always scheduled for widget updates & on-time notifications).
         */
        fun scheduleExact(context: Context, prayerTime: PrayerTime) {
            scheduleAlarm(context, prayerTime, timestamp = prayerTime.getEpochMilli(), isExact = true)
        }

        /**
         * Schedule pre-Adhan alarm before prayer time (e.g. 10m before).
         */
        fun schedulePreAdhan(context: Context, prayerTime: PrayerTime) {
            val offsetMilli = NotificationOffset.get(context).getMilli()
            if (offsetMilli <= 0) return
            val targetTime = prayerTime.getEpochMilli() - offsetMilli
            if (targetTime <= System.currentTimeMillis()) return
            scheduleAlarm(context, prayerTime, timestamp = targetTime, isExact = false)
        }

        /**
         * Unified schedule helper to schedule exact and pre-Adhan alarms.
         */
        fun schedule(context: Context, prayerTime: PrayerTime) {
            scheduleExact(context, prayerTime)
            if (prayerTime.type.shouldNotify() && NotificationOffset.isEnabled(context)) {
                schedulePreAdhan(context, prayerTime)
            }
        }

        /**
         * Schedule a test notification for a testing prayer time.
         */
        fun scheduleTestNotification(context: Context, delay: Long) {
            Log.i(TAG, "Scheduling test notification with a $delay minutes delay")
            val testPrayerTime = PrayerTime(
                method = PrayerTimeMethod.get(context),
                city = PrayerTimeCity.get(context),
                type = PrayerTimeType.asr,
                time = LocalDateTime.now().plusMinutes(delay).withSecond(0).withNano(0)
            )
            schedule(context, testPrayerTime)
        }

        private fun scheduleAlarm(context: Context, prayerTime: PrayerTime, timestamp: Long, isExact: Boolean) {
            if (timestamp <= System.currentTimeMillis()) return

            val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
            if (!AlarmManagerCompat.canScheduleExactAlarms(alarmManager)) {
                Log.w(TAG, "Cannot schedule exact alarms")
                return
            }

            val alarmIntent = buildIntent(context, prayerTime, isExact)
            AlarmManagerCompat.setAlarmClock(
                alarmManager,
                timestamp,
                alarmIntent,
                alarmIntent
            )
        }

        private fun buildIntent(context: Context, prayerTime: PrayerTime, isExact: Boolean): PendingIntent {
            val intent = Intent(context, NotificationWorker::class.java).apply {
                action = ACTION
                putExtra(INPUT_PRAYER_TIME_ID, prayerTime.getStringId())
                putExtra(INPUT_IS_EXACT, isExact)
            }
            val requestCode = if (isExact) prayerTime.getIntId() * 2 + 1 else prayerTime.getIntId() * 2
            return PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

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
