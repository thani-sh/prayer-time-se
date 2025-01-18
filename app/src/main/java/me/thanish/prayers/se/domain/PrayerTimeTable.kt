package me.thanish.prayers.se.domain

import android.content.Context
import java.time.LocalDate

/**
 * PrayerTimeTable is a data class representing a prayer time table.
 * This contains all the prayer times for a given date and given city.
 */
data class PrayerTimeTable(
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
        fun forDate(context: Context, city: PrayerTimeCity, date: LocalDate): PrayerTimeTable {
            val method = PrayerTimeMethod.get(context)
            val data = PrayerTimeData.get(context, city)

            return PrayerTimeTable(
                city,
                date,
                PrayerTime(city, PrayerTimeType.fajr, data.getFajr(date)),
                PrayerTime(city, PrayerTimeType.shuruk, data.getShuruk(date)),
                PrayerTime(city, PrayerTimeType.dhohr, data.getDhohr(date)),
                PrayerTime(city, PrayerTimeType.asr, data.getAsr(date, method)),
                PrayerTime(city, PrayerTimeType.maghrib, data.getMaghrib(date)),
                PrayerTime(city, PrayerTimeType.isha, data.getIsha(date)),
            )
        }

        /**
         * forToday creates a prayer time table for today.
         */
        fun forToday(context: Context, city: PrayerTimeCity): PrayerTimeTable {
            return forDate(context, city, LocalDate.now())
        }
    }
}
