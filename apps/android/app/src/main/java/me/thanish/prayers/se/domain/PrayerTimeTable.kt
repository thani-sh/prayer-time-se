package me.thanish.prayers.se.domain

import android.content.Context
import java.time.LocalDate

/**
 * PrayerTimeTable is a data class representing a prayer time table.
 * This contains all the prayer times for a given date and given city.
 */
data class PrayerTimeTable(
    val method: PrayerTimeMethod,
    val city: PrayerTimeCity,
    val date: LocalDate,
    val fajr: PrayerTime,
    val shuruk: PrayerTime,
    val dhohr: PrayerTime,
    val asr: PrayerTime,
    val maghrib: PrayerTime,
    val isha: PrayerTime,
) {
    /**
     * toList returns a list of prayer times in order.
     */
    fun toList(): List<PrayerTime> {
        return listOf(fajr, shuruk, dhohr, asr, maghrib, isha)
    }

    /**
     * ✄ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     */

    companion object {
        /**
         * fromAssetData creates a prayer time table from asset data.
         */
        fun forDate(context: Context, method: PrayerTimeMethod, city: PrayerTimeCity, date: LocalDate): PrayerTimeTable {
            val data = PrayerTimeData.get(context, method, city)

            return PrayerTimeTable(
                method,
                city,
                date,
                PrayerTime(method, city, PrayerTimeType.fajr, data.getFajr(date)),
                PrayerTime(method, city, PrayerTimeType.shuruk, data.getShuruk(date)),
                PrayerTime(method, city, PrayerTimeType.dhohr, data.getDhohr(date)),
                PrayerTime(method, city, PrayerTimeType.asr, data.getAsr(date)),
                PrayerTime(method, city, PrayerTimeType.maghrib, data.getMaghrib(date)),
                PrayerTime(method, city, PrayerTimeType.isha, data.getIsha(date)),
            )
        }

        /**
         * forToday creates a prayer time table for today.
         */
        fun forToday(context: Context, method: PrayerTimeMethod, city: PrayerTimeCity): PrayerTimeTable {
            return forDate(context, method, city, LocalDate.now())
        }
    }
}
