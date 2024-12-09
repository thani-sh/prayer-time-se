package me.thanish.prayers.se.states

import com.tencent.mmkv.MMKV;


/**
 * data class to store user preferences
 */
data class Preferences(var city: String)

/**
 * use mmkv to persist user preferences
 */
private val store = MMKV.defaultMMKV();

/**
 * preferences cached in memory for fast access
 */
private val preferences = initPreferences()

/**
 * default city to use when it is not set
 */
private const val DEFAULT_CITY = "Stockholm"

/**
 * get the selected city
 */
fun getCity(): String {
    return preferences.city
}

/**
 * set the selected city
 */
fun setCity(city: String) {
    preferences.city = city
    store.putString("city", city)
}

/**
 * read stored preferences and initialize
 */
private fun initPreferences(): Preferences {
    var city = store.getString("city", DEFAULT_CITY)
    if (city == null) {
        // NOTE: doing this check shouldn't be necessary
        city = DEFAULT_CITY
    }
    return Preferences(city)
}
