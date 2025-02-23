package me.thanish.prayers.se.domain

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import me.thanish.prayers.se.R

/**
 * HijriCalendarOffset is the offset for the hijri calendar.
 */
class HijriCalendarOffset(val offset: Int) {
    /**
     * getLabel returns the name of the offset with i18n.
     */
    fun getLabel(context: Context): String {
        if (offset == DISABLED_OFFSET) {
            return context.getString(R.string.hijri_calendar_offset_disabled)
        }
        val template = context.getString(R.string.hijri_calendar_offset_template)
        return String.format(template, offset)
    }

    /**
     * ✄ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     */

    companion object {
        /**
         * DISABLED_OFFSET is the offset for the hijri calendar.
         */
        const val DISABLED_OFFSET: Int = 0

        /**
         * DEFAULT_OFFSET is the default offset in minutes.
         */
        const val DEFAULT_OFFSET: Int = DISABLED_OFFSET

        /**
         * MIN_OFFSET is the minimum offset in minutes.
         */
        const val MIN_OFFSET: Int = -3

        /**
         * MAX_OFFSET is the maximum offset in minutes.
         */
        const val MAX_OFFSET: Int = 3

        /**
         * STORE_KEY is the MMKV key for storing the hijri calendar offset.
         */
        private val STORE_KEY = intPreferencesKey("HijriCalendarOffset")

        /**
         * set sets the notification offset to a specific value.
         */
        fun set(context: Context, offset: HijriCalendarOffset) {
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
         * get returns the notification offset.
         */
        fun get(context: Context): HijriCalendarOffset {
            val offset = getIntegerSync(context, STORE_KEY, DISABLED_OFFSET)
            return HijriCalendarOffset(offset)
        }
    }
}
