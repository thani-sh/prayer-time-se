package me.thanish.prayers.se.times

import android.content.Context
import com.google.gson.Gson

/**
 * cache data in memory to access them faster after they are loaded once
 */
var cachedCity: String? = null
var cachedData: Array<Array<Array<Int>>>? = null
var cachedList: Array<String>? = null

/**
 * read an asset file from the src/main/assets directory as a JSONObject
 */
private fun readAssetFile(context: Context, filePath: String): String {
    return context.assets.open(filePath).bufferedReader().use { it.readText() }
}

/**
 * read a list of cities supported by the app from assets directory
 */
internal fun readCities(context: Context): Array<String> {
    // NOTE: read data from memory
    if (cachedList != null)
        return cachedList as Array<String>

    // NOTE: read data from a file
    val text = readAssetFile(context, "cities.json")
    val data = Gson().fromJson(text, Array<String>::class.java)

    // NOTE: cache data in memory
    cachedList = data

    return data
}

/**
 * read prayer times for the given city in a compressed data format
 */
internal fun readTimes(context: Context, city: String): Array<Array<Array<Int>>> {
    // NOTE: read data from memory
    if (cachedData != null && cachedCity == city)
        return cachedData as Array<Array<Array<Int>>>

    // NOTE: read data from a file
    val text = readAssetFile(context, "values/$city.json")
    val data = Gson().fromJson(text, Array<Array<Array<Int>>>::class.java)

    // NOTE: cache data in memory
    cachedCity = city
    cachedData = data

    return data
}
