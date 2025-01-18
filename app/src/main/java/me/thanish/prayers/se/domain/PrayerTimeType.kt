package me.thanish.prayers.se.domain

import android.content.Context
import me.thanish.prayers.se.R

/**
 * PrayerTimeType is the type of a prayer time listed in the app.
 * Note: This also includes "sunrise" which is not a prayer time.
 */
enum class PrayerTimeType(private val key: Int, private val notify: Boolean = true) {
    fajr(R.string.prayers_type_fajr),
    shuruk(R.string.prayers_type_shuruk, false),
    dhohr(R.string.prayers_type_dhohr),
    asr(R.string.prayers_type_asr),
    maghrib(R.string.prayers_type_maghrib),
    isha(R.string.prayers_type_isha);

    /**
     * getLabel returns the name of the prayer time with i18n.
     */
    fun getLabel(context: Context): String {
        return context.getString(key)
    }

    /**
     * shouldNotify returns whether the prayer time should be notified.
     */
    fun shouldNotify(): Boolean {
        return notify
    }
}
