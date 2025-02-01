//
//  PrayerTimeTable.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//


import Foundation

// PrayerTimeTable is a helper struct that makes it easy to bundle prayer times
// for a specific date together. Provides computed properties for easy access.
struct PrayerTimeTable {
  // Get the PrayerTimeTable for given date and city.
  static func forDate(city: PrayerTimeCity, date: Date) -> PrayerTimeTable {
    let values = PrayerTime.getPrayersForDate(city: city, date: date)
    return PrayerTimeTable.init(city: city, date: date, data: values)
  }
  
  // MARK: - Properties
  
  let city: PrayerTimeCity
  let date: Date
  let data: [PrayerTime]
  
  // MARK: - Computed Properties
  
  var fajr: PrayerTime { data.first(where: { $0.type == .fajr })! }
  var shuruk: PrayerTime { data.first(where: { $0.type == .shuruk })! }
  var dhohr: PrayerTime { data.first(where: { $0.type == .dhohr })! }
  var asr: PrayerTime { data.first(where: { $0.type == .asr })! }
  var maghrib: PrayerTime { data.first(where: { $0.type == .maghrib })! }
  var isha: PrayerTime { data.first(where: { $0.type == .isha })! }
  
  // MARK: - Initializers
  
  init(city: PrayerTimeCity, date: Date, data: [PrayerTime]) {
    self.city = city
    self.date = date
    self.data = data
  }
}
