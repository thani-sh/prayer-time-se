package me.thanish.prayers.se.times

import android.content.Context
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.floor


/**
 * a data class to represent prayer times for a date
 */
data class PrayerTimes(
    val city: String,
    val fajr: LocalTime,
    val shuruk: LocalTime,
    val dhohr: LocalTime,
    val asr: LocalTime,
    val maghrib: LocalTime,
    val isha: LocalTime,
)

/**
 * converts a compressed prayer time value to a local time
 */
private fun toPrayerTime(minutes: Int): LocalTime {
    val h = floor((minutes / 60).toDouble()).toInt()
    val m = minutes % 60
    return LocalTime.of(h, m)
}

/**
 * get a list of supported cities
 */
fun getSupportedCities(context: Context): Array<String> {
    return readCities(context)
}

/**
 * get prayer times for city and date
 */
fun getPrayerTimesForDate(context: Context, city: String, date: LocalDate = LocalDate.now()): PrayerTimes {
    val times = readTimes(context, city)[date.monthValue - 1][date.dayOfMonth - 1]
    return PrayerTimes(
        city,
        toPrayerTime(times[0]),
        toPrayerTime(times[1]),
        toPrayerTime(times[2]),
        toPrayerTime(times[3]),
        toPrayerTime(times[4]),
        toPrayerTime(times[5]),
    )
}
