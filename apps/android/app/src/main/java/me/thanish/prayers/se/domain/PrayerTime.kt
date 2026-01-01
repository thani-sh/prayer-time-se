package me.thanish.prayers.se.domain

import android.content.Context
import me.thanish.prayers.se.R
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * PrayerTime is a data class representing a prayer time.
 */
data class PrayerTime(
    val method: PrayerTimeMethod,
    val city: PrayerTimeCity,
    val type: PrayerTimeType,
    val time: LocalDateTime
) {
    /**
     * getIntId returns a unique integer id for the prayer time.
     */
    fun getIntId(): Int {
        return getStringId().hashCode()
    }

    /**
     * getStringId returns a unique string id for the prayer time.
     */
    fun getStringId(): String {
        return "${method}|${city}|${type}|${time}"
    }

    /**
     * getEpochSeconds returns the epoch seconds for the prayer time.
     */
    fun getEpochSeconds(): Int {
        val zone = ZoneId.systemDefault()
        return time.atZone(zone).toInstant().epochSecond.toInt()
    }

    /**
     * getEpochMilli returns the epoch milli seconds for the prayer time.
     */
    fun getEpochMilli(): Long {
        val zone = ZoneId.systemDefault()
        return time.atZone(zone).toInstant().toEpochMilli()
    }

    /**
     * getTimeString returns the time string for the prayer time without date.
     */
    fun getTimeString(context: Context): String {
        return TimeFormat.get(context).format(time.toLocalTime())
    }

    /**
     * getUntilString returns the time string for the prayer time from system time.
     * Eg: "in 32min", "in 1h 20min", etc.
     */
    fun getUntilString(context: Context): String {
        val now = LocalDateTime.now()
        val duration = java.time.Duration.between(now, time).seconds
        val m = (duration / 60) % 60
        val h = duration / (60 * 60)
        if (h > 0) {
            return context.resources.getString(R.string.prayers_time_until_hm, h, m)
        }
        return context.resources.getString(R.string.prayers_time_until_m, m)
    }

    /**
     * isCurrentPrayer returns true if the prayer time is the current prayer time.
     */
    fun isCurrentPrayer(): Boolean {
        val now = LocalDateTime.now()
        return now > time && now.plusSeconds(ACTIVE_DURATION_SECONDS) < time
    }

    /**
     * isNextPrayer returns true if the prayer time is the next prayer time.
     */
    fun isNextPrayer(context: Context): Boolean {
        return getNext(context, method, city, 1).firstOrNull() == this
    }

    /**
     * ✄ - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     */

    companion object {
        /**
         * ACTIVE_DURATION_SECONDS is the duration to consider a prayer as current.
         */
        const val ACTIVE_DURATION_SECONDS: Long = 30 * 60

        /**
         * fromStringId creates a prayer time from a string id.
         */
        fun fromStringId(id: String): PrayerTime? {
            val parts = id.split("|")
            if (parts.size != 4) {
                return null
            }
            val method = PrayerTimeMethod.valueOf(parts[0])
            val city = PrayerTimeCity.valueOf(parts[1])
            val type = PrayerTimeType.valueOf(parts[2])
            val time = LocalDateTime.parse(parts[3])
            return PrayerTime(method, city, type, time)
        }


        /**
         * getNextPrayer returns the next prayer time
         */
        fun getNextPrayer(context: Context, method: PrayerTimeMethod, city: PrayerTimeCity): PrayerTime {
            val item = getNext(context, method, city, 1).firstOrNull()
                ?: throw Exception("No prayer times found for $city ($method)")
            return item
        }

        /**
         * getNext returns the next N prayer times starting from the given datetime.
         */
        fun getNext(
            context: Context,
            method: PrayerTimeMethod,
            city: PrayerTimeCity,
            count: Int,
            from: LocalDateTime = LocalDateTime.now()
        ): List<PrayerTime> {
            val list = mutableListOf<PrayerTime>()
            var date = from.toLocalDate()
            while (list.size < count) {
                val prayerTimes = PrayerTimeTable.forDate(context, method, city, date).toList()
                for (prayerTime in prayerTimes) {
                    if (prayerTime.time.isBefore(from)) {
                        continue
                    }
                    list.add(prayerTime)
                }
                date = date.plusDays(1)
            }
            return list.take(count)
        }
    }
}
