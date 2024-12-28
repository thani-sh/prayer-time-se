package me.thanish.prayers.se.domain

import android.content.Context
import com.tencent.mmkv.MMKV
import me.thanish.prayers.se.R

/**
 * NotificationOffset is the offset for notifications in minutes.
 * Currently this only supports notifying before the prayer time.
 */
class NotificationOffset(val offset: Int) {
    /**
     * getLabel returns the name of the city with i18n.
     */
    fun getLabel(context: Context): String {
        if (offset == DISABLED_OFFSET) {
            return context.getString(R.string.notification_offset_disabled)
        }
        val template = context.getString(R.string.notification_offset_template)
        return String.format(template, offset)
    }

    /**
     * getMilli returns the offset in milliseconds.
     */
    fun getMilli(): Int {
        return offset * 60 * 1000
    }

    /**
     * ✄ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     */

    companion object {
        /**
         * DISABLED_OFFSET is the offset when notifications are disabled.
         */
        private const val DISABLED_OFFSET: Int = -1

        /**
         * STORE_KEY is the MMKV key for storing the notification offset.
         */
        private const val STORE_KEY = "NotificationOffset"

        /**
         * store is the MMKV instance for storing the notification offset.
         */
        private val store = MMKV.defaultMMKV();

        /**
         * entries is the list of notification offsets suggested by the app.
         */
        val entries: List<NotificationOffset>
            get() = listOf(
                NotificationOffset(DISABLED_OFFSET),
                NotificationOffset(10),
                NotificationOffset(15),
                NotificationOffset(20),
            )

        /**
         * set sets the notification offset.
         */
        fun set(offset: NotificationOffset) {
            store.putInt(STORE_KEY, offset.offset)
        }

        /**
         * get returns the notification offset.
         */
        fun get(): NotificationOffset {
            return NotificationOffset(store.getInt(STORE_KEY, DISABLED_OFFSET))
        }

        /**
         * isEnabled returns true if notifications are enabled.
         */
        fun isEnabled(): Boolean {
            return get().offset != DISABLED_OFFSET
        }
    }
}
