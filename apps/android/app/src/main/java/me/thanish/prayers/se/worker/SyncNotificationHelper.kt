package me.thanish.prayers.se.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import me.thanish.prayers.se.R

object SyncNotificationHelper {
    private const val SYNC_CH_ID = "sync_progress"
    private const val NOTIFICATION_ID = 1001

    fun initialize(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)
        val channelName = context.getString(R.string.route_settings_sync_data)
        val channelPrio = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(SYNC_CH_ID, channelName, channelPrio)
        manager.createNotificationChannel(channel)
    }

    fun showProgress(context: Context, current: Int, total: Int) {
        val manager = context.getSystemService(NotificationManager::class.java)
        val progressText = context.getString(R.string.route_settings_sync_data_progress, current, total)
        
        val notification = NotificationCompat.Builder(context, SYNC_CH_ID)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle(context.getString(R.string.route_settings_sync_data))
            .setContentText(progressText)
            .setProgress(total, current, false)
            .setOngoing(true)
            .build()
            
        manager.notify(NOTIFICATION_ID, notification)
    }

    fun clear(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.cancel(NOTIFICATION_ID)
    }
}
