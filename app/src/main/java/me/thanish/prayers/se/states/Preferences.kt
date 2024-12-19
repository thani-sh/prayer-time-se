package me.thanish.prayers.se.states

import com.tencent.mmkv.MMKV;

/**
 * use mmkv to persist user preferences
 */
private val store = MMKV.defaultMMKV();

/**
 * data class to store user preferences
 */
data class Preferences(val city: String) {
    companion object {
        private const val DEFAULT_CITY = "Stockholm"

        /**
         * get the selected city
         */
        fun getCity(): String {
            return store.getString("city", DEFAULT_CITY) as String
        }

        /**
         * set the selected city
         */
        fun setCity(city: String) {
            store.putString("city", city)
        }
    }
}
