//
//  PrayerTimeTable.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//


import Foundation

struct PrayerTimeTable {
    let city: PrayerTimeCity
    let date: Date
    let fajr: PrayerTime
    let shuruk: PrayerTime
    let dhohr: PrayerTime
    let asr: PrayerTime
    let maghrib: PrayerTime
    let isha: PrayerTime
    
    var prayerTimes: [PrayerTime] {
        [fajr, shuruk, dhohr, asr, maghrib, isha]
    }
    
    static func forDate(city: PrayerTimeCity, date: Date) -> PrayerTimeTable {
        let data = PrayerTimeData.get(for: city)
        
        return PrayerTimeTable(
            city: city,
            date: date.startOfDay,
            fajr: PrayerTime(
                city: city,
                type: .fajr,
                time: data.fajrTime(for: date)
            ),
            shuruk: PrayerTime(
                city: city,
                type: .shuruk,
                time: data.shurukTime(for: date)
            ),
            dhohr: PrayerTime(
                city: city,
                type: .dhohr,
                time: data.dhohrTime(for: date)
            ),
            asr: PrayerTime(
                city: city,
                type: .asr,
                time: data.asrTime(for: date)
            ),
            maghrib: PrayerTime(
                city: city,
                type: .maghrib,
                time: data.maghribTime(for: date)
            ),
            isha: PrayerTime(
                city: city,
                type: .isha,
                time: data.ishaTime(for: date)
            )
        )
    }
    
    static func forToday(city: PrayerTimeCity) -> PrayerTimeTable {
        let now = Date()
        return forDate(city: city, date: now)
    }
}

// MARK: - Date Helpers
extension Date {
    var startOfDay: Date {
        Calendar.current.startOfDay(for: self)
    }
}
