package me.thanish.prayers.se.domain

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import me.thanish.prayers.se.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * TimeFormat is the format of the time.
 */
enum class TimeFormat(private val key: Int) {
    h12(R.string.time_format_12h),
    h24(R.string.time_format_24h);

    /**
     * getLabel returns the name of the format with i18n.
     */
    fun getLabel(context: Context): String {
        return context.getString(key)
    }

    /**
     * format returns the formatted time string.
     */
    fun format(time: LocalTime): String {
        if (this == h24) {
            return time.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
        return time.format(DateTimeFormatter.ofPattern("h:mm a"))
    }

    companion object {
        /**
         * STORE_KEY is the MMKV key for storing the current time format.
         */
        private val STORE_KEY = intPreferencesKey("TimeFormat")


        /**
         * set sets the current time format.
         */
        fun set(context: Context, format: TimeFormat) {
            setIntegerSync(context, STORE_KEY, format.ordinal)
        }

        /**
         * get returns the current time format.
         */
        fun get(context: Context): TimeFormat {
            val index = getIntegerSync(context, STORE_KEY, h24.ordinal)
            return entries[index]
        }
    }
}
