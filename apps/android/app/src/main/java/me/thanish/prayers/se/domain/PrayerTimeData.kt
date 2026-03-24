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
    val method: PrayerTimeMethod,
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
        private const val INDEX_MAGHRIB: Int = 4
        private const val INDEX_ISHA: Int = 5

        /**
         * current keeps the prayer times in memory for faster access.
         */
        private var current: PrayerTimeData? = null

        /**
         * clearCache clears the in-memory prayer times.
         */
        fun clearCache() {
            current = null
        }

        /**
         * fromAssetData reads prayer times for the given city and returns
         * a multi-dimensional array with this format.
         *   - 1st Index: Month ( 0 - 11 )
         *   - 2nd Index: Day of Month ( 0 - 30 )
         *   - 3rd Index: Prayer Time ( 0 - 5 ) [fajr, shuruk, dhohr, asr, maghrib, isha]
         *   - Values:    Minutes from 00:00
         */
        fun get(context: Context, method: PrayerTimeMethod, city: PrayerTimeCity): PrayerTimeData {
            if (current == null || current!!.city != city || current!!.method != method) {
                current = PrayerTimeData(method, city, readData(context, method, city))
            }
            return current as PrayerTimeData
        }

        /**
         * readData reads prayer times from the database if available,
         * otherwise it falls back to the asset file.
         */
        private fun readData(context: Context, method: PrayerTimeMethod, city: PrayerTimeCity): Array<Array<Array<Int>>> {
            try {
                val db = me.thanish.prayers.se.storage.AppDatabase.getDatabase(context)
                val dao = db.prayerTimeDao()
                val entities = dao.forCity(city.name)

                if (entities.isNotEmpty()) {
                    val data = Array(12) { Array(31) { Array(6) { 0 } } }
                    for (entity in entities) {
                        if (entity.month < 12 && entity.day < 31) {
                            data[entity.month][entity.day][0] = entity.fajr
                            data[entity.month][entity.day][1] = entity.sunrise
                            data[entity.month][entity.day][2] = entity.dhuhr
                            data[entity.month][entity.day][3] = entity.asr
                            data[entity.month][entity.day][4] = entity.maghrib
                            data[entity.month][entity.day][5] = entity.isha
                        }
                    }
                    return data
                }
            } catch (_: Exception) {
                // Fallback to assets
            }

            return readAssetFile(context, method, city)
        }

        /**
         * readAssetFile reads a text file from the assets directory
         */
        private fun readAssetFile(context: Context, method: PrayerTimeMethod, city: PrayerTimeCity): Array<Array<Array<Int>>> {
            val path = "values/$method.$city.json"
            val text = context.assets.open(path).bufferedReader().use { it.readText() }
            return Gson().fromJson(text, Array<Array<Array<Int>>>::class.java)
        }
    }
}
