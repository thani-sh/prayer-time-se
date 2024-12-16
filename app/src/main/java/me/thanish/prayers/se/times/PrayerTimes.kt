package me.thanish.prayers.se.times

import android.content.Context
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import kotlin.math.floor


/**
 * a data class to represent prayer times for a date
 */
data class PrayerTimes(
    val city: String,
    val date: LocalDate,
    val fajr: PrayerTime,
    val shuruk: PrayerTime,
    val dhohr: PrayerTime,
    val asr: PrayerTime,
    val maghrib: PrayerTime,
    val isha: PrayerTime,
) {
    fun toList(): List<PrayerTime> {
        return listOf(fajr, shuruk, dhohr, asr, maghrib, isha)
    }
}

/**
 * a data class to represent a specific prayer time
 */
data class PrayerTime(
    val city: String,
    val name: String,
    val time: LocalDateTime,
) {
    fun serialize(): String {
        return "PrayerTime|$city|$name|$time"
    }

    fun toEpochSeconds(): Int {
        val zone = ZoneId.systemDefault()
        return time.atZone(zone).toInstant().epochSecond.toInt()
    }

    fun toEpochMilli(): Long {
        val zone = ZoneId.systemDefault()
        return time.atZone(zone).toInstant().toEpochMilli()
    }

    companion object {
        fun deserialize(id: String): PrayerTime {
            val parts = id.split("|")
            val city = parts[1]
            val name = parts[2]
            val time = LocalDateTime.parse(parts[3])
            return PrayerTime(city, name, time)
        }
    }
}

/**
 * converts a compressed prayer time value to a local time
 */
private fun toPrayerTime(city: String, date: LocalDate, name: String, minutes: Int): PrayerTime {
    val h = floor((minutes / 60).toDouble()).toInt()
    val m = minutes % 60
    val t = LocalTime.of(h, m).atDate(date)
    return PrayerTime(city, name, t)
}

/**
 * get a list of supported cities
 */
fun getSupportedCities(context: Context): Array<String> {
    return readCities(context)
}

/**
 * Get prayer times for given city and date
 */
fun getPrayerTimesForDate(context: Context, city: String, date: LocalDate): PrayerTimes {
    val times = readTimes(context, city)[date.monthValue - 1][date.dayOfMonth - 1]
    return PrayerTimes(
        city,
        date,
        toPrayerTime(city, date, name = "fajr", times[0]),
        toPrayerTime(city, date, name = "shuruk", times[1]),
        toPrayerTime(city, date, name = "dhohr", times[2]),
        toPrayerTime(city, date, name = "asr", times[3]),
        toPrayerTime(city, date, name = "maghrib", times[4]),
        toPrayerTime(city, date, name = "isha", times[5]),
    )
}

/**
 * Get next N prayer times starting from given datetime
 */
fun getNextPrayerTimes(
    context: Context,
    city: String,
    count: Int,
    from: LocalDateTime = LocalDateTime.now()
): List<PrayerTime> {
    val list = mutableListOf<PrayerTime>()
    var date = from.toLocalDate()
    while (list.size < count) {
        for (prayerTime in getPrayerTimesForDate(context, city, date).toList()) {
            if (prayerTime.time.isBefore(from)) {
                continue
            }
            list.add(prayerTime)
        }
        date = date.plusDays(1)
    }

    return list.take(count)
}
