package me.thanish.prayers.se.domain

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
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
     * isEnabled returns true if notifications are enabled.
     */
    fun isEnabled(): Boolean {
        return offset != DISABLED_OFFSET
    }

    /**
     * ✄ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     */

    companion object {
        /**
         * DISABLED_OFFSET is the offset when notifications are disabled.
         */
        const val DISABLED_OFFSET: Int = -1

        /**
         * DEFAULT_OFFSET is the default offset in minutes.
         */
        const val DEFAULT_OFFSET: Int = 5

        /**
         * MIN_OFFSET is the minimum offset in minutes.
         */
        const val MIN_OFFSET: Int = 0

        /**
         * MAX_OFFSET is the maximum offset in minutes.
         */
        const val MAX_OFFSET: Int = 30

        /**
         * STORE_KEY is the MMKV key for storing the notification offset.
         */
        private val STORE_KEY = intPreferencesKey("NotificationOffset")

        /**
         * set sets the notification offset to a specific value.
         */
        fun set(context: Context, offset: NotificationOffset) {
            val value = if (offset.offset == DISABLED_OFFSET) {
                DISABLED_OFFSET
            } else if (offset.offset < MIN_OFFSET) {
                MIN_OFFSET
            } else if (offset.offset > MAX_OFFSET) {
                MAX_OFFSET
            } else {
                offset.offset
            }
            setIntegerSync(context, STORE_KEY, value)
        }

        /**
         * setEnabled sets the notification offset based on whether notifications are enabled.
         */
        fun set(context: Context, enabled: Boolean) {
            val value = if (enabled) DEFAULT_OFFSET else DISABLED_OFFSET
            setIntegerSync(context, STORE_KEY, value)
        }

        /**
         * get returns the notification offset.
         */
        fun get(context: Context): NotificationOffset {
            val offset = getIntegerSync(context, STORE_KEY, DISABLED_OFFSET)
            return NotificationOffset(offset)
        }

        /**
         * isEnabled returns true if notifications are enabled.
         */
        fun isEnabled(context: Context): Boolean {
            val offset = getIntegerSync(context, STORE_KEY, DISABLED_OFFSET)
            return offset != DISABLED_OFFSET
        }
    }
}
