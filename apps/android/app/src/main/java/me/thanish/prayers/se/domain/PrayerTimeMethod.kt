package me.thanish.prayers.se.domain

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.thanish.prayers.se.R

/**
 * PrayerTimeMethod is the calculation method for prayer times.
 */
enum class PrayerTimeMethod(
    private val labelKey: Int,
    private val descriptionKey: Int? = null
) {
    islamiskaforbundet(
        R.string.prayers_method_islamiskaforbundet,
        R.string.prayers_method_islamiskaforbundet_details
    );

    /**
     * getLabel returns the name of the prayer method with i18n.
     */
    fun getLabel(context: Context): String {
        return context.getString(labelKey)
    }

    /**
     * getDescription returns the description of the prayer method with i18n.
     */
    fun getDescription(context: Context): String? {
        if (descriptionKey == null) {
            return null
        }
        return context.getString(descriptionKey)
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
            CoroutineScope(Dispatchers.IO).launch {
                PrayerTimeRepository.syncIfNeeded(context)
            }
        }

        /**
         * get returns the current prayer method.
         */
        fun get(context: Context): PrayerTimeMethod {
            val index = getIntegerSync(context, STORE_KEY, islamiskaforbundet.ordinal)
            return entries[index]
        }
    }
}
