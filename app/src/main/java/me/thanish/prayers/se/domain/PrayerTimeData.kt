package me.thanish.prayers.se.domain

import android.content.Context
import com.google.gson.Gson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * PrayerTimeData contains data read from asset json files.
 */
internal class PrayerTimeData(
    val city: PrayerTimeCity,
    private val data: Array<Array<Array<Int>>>
) {
    /**
     * getFajr returns the fajr prayer time as a LocalDateTime.
     */
    fun getFajr(date: LocalDate): LocalDateTime {
        return getPrayerTime(date, INDEX_FAJR)
    }

    /**
     * getShuruk returns the shuruk prayer time as a LocalDateTime.
     */
    fun getShuruk(date: LocalDate): LocalDateTime {
        return getPrayerTime(date, INDEX_SHURUK)
    }

    /**
     * getDhohr returns the dhohr prayer time as a LocalDateTime.
     */
    fun getDhohr(date: LocalDate): LocalDateTime {
        return getPrayerTime(date, INDEX_DHOHR)
    }

    /**
     * getAsr returns the asr prayer time as a LocalDateTime.
     */
    fun getAsr(date: LocalDate): LocalDateTime {
        return getPrayerTime(date, INDEX_ASR_SHAFI)
    }

    /**
     * getMaghrib returns the maghrib prayer time as a LocalDateTime.
     */
    fun getMaghrib(date: LocalDate): LocalDateTime {
        return getPrayerTime(date, INDEX_MAGHRIB)
    }

    /**
     * getIsha returns the isha prayer time as a LocalDateTime.
     */
    fun getIsha(date: LocalDate): LocalDateTime {
        return getPrayerTime(date, INDEX_ISHA)
    }

    /**
     * getPrayerTime returns the prayer time for the given date and index.
     */
    private fun getPrayerTime(date: LocalDate, index: Int): LocalDateTime {
        val minutes = data[date.monthValue - 1][date.dayOfMonth - 1][index]
        return LocalTime.of(minutes / 60, minutes % 60).atDate(date)
    }

    /**
     * ✄ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     */

    companion object {
        private const val INDEX_FAJR: Int = 0
        private const val INDEX_SHURUK: Int = 1
        private const val INDEX_DHOHR: Int = 2
        private const val INDEX_ASR_SHAFI: Int = 3
        private const val INDEX_ASR_HANAFI: Int = 4
        private const val INDEX_MAGHRIB: Int = 5
        private const val INDEX_ISHA: Int = 6

        /**
         * current keeps the prayer times in memory for faster access.
         */
        private var current: PrayerTimeData? = null

        /**
         * fromAssetData reads prayer times for the given city and returns
         * a multi-dimensional array with this format.
         *   - 1st Index: Month ( 0 - 11 )
         *   - 2nd Index: Day of Month ( 0 - 30 )
         *   - 3rd Index: Prayer Time ( 0 - 6 ) [fajr, shuruk, dhohr, asr, asr_hanafi, maghrib, isha]
         *   - Values:    Minutes from 00:00
         */
        fun get(context: Context, city: PrayerTimeCity): PrayerTimeData {
            if (current == null || current!!.city != city) {
                current = PrayerTimeData(city, readAssetFile(context, city))
            }
            return current as PrayerTimeData
        }

        /**
         * readAssetFile reads a text file from the assets directory
         */
        private fun readAssetFile(context: Context, city: PrayerTimeCity): Array<Array<Array<Int>>> {
            val path = "values/$city.json"
            val text = context.assets.open(path).bufferedReader().use { it.readText() }
            return Gson().fromJson(text, Array<Array<Array<Int>>>::class.java)
        }
    }
}
