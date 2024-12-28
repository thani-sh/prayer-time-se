package me.thanish.prayers.se.domain

import android.content.Context
import com.tencent.mmkv.MMKV
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
        private const val STORE_KEY = "PrayerTimeMethod"

        /**
         * store is the MMKV instance for storing the current prayer method.
         */
        private val store = MMKV.defaultMMKV();

        /**
         * set sets the current prayer method.
         */
        fun set(method: PrayerTimeMethod) {
            store.putInt(STORE_KEY, method.ordinal)
        }

        /**
         * get returns the current prayer method.
         */
        fun get(): PrayerTimeMethod {
            val index = store.getInt(STORE_KEY, shafi.ordinal)
            return entries[index]
        }
    }
}
