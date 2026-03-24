package me.thanish.prayers.se.storage

import androidx.room.Entity

@Entity(tableName = "prayer_times", primaryKeys = ["month", "day", "city"])
data class PrayerTimeEntity(
    val month: Int,
    val day: Int,
    val city: String,
    val fajr: Int,
    val sunrise: Int,
    val dhuhr: Int,
    val asr: Int,
    val maghrib: Int,
    val isha: Int
)
