package me.thanish.prayers.se.domain

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import me.thanish.prayers.se.R

/**
 * PrayerTimeMethod is the calculation method for prayer times.
 */
enum class PrayerTimeMethod(private val key: Int) {
    shafi(R.string.prayers_method_shafi),
    hanafi(R.string.prayers_method_hanafi);

    /**
     * getLabel returns the name of the prayer method with i18n.
     */
    fun getLabel(context: Context): String {
        return context.getString(key)
    }

    /**
     * ✄ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     */

    companion object {
        /**
         * STORE_KEY is the MMKV key for storing the current prayer method.
         */
        private val STORE_KEY = intPreferencesKey("PrayerTimeMethod")

        /**
         * set sets the current prayer method.
         */
        fun set(context: Context, method: PrayerTimeMethod) {
            setIntegerSync(context, STORE_KEY, method.ordinal)
        }

        /**
         * get returns the current prayer method.
         */
        fun get(context: Context): PrayerTimeMethod {
            val index = getIntegerSync(context, STORE_KEY, shafi.ordinal)
            return entries[index]
        }
    }
}
