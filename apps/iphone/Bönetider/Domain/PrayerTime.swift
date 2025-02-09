//
//  PrayerTime.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//

import SwiftUI

// PrayerTime is a struct representing a specific prayer time. It contains
// required data, several useful computed properties and methods to re-use
// business logic.
struct PrayerTime: Identifiable, Equatable {
  // Consider the prayer time is current for 30 minutes from adhan
  static let activeDuration: TimeInterval = 60 * 30
  
  // Notification offsets are equal if the number of mintues are equal
  static func == (lhs: PrayerTime, rhs: PrayerTime) -> Bool {
    lhs.id == rhs.id
  }
  
  // Get returns all prayers for the given date as an ordered array.
  // TODO: the values[4] should be removed from bundled json files.
  static func getPrayersForDate(method: PrayerTimeMethod, city: PrayerTimeCity, date: Date = Date()) -> [PrayerTime] {
    let values = PrayerTimeData.current.forDate(date)
    return [
      PrayerTime(method: method, city: city, type: .fajr, time: values[0]),
      PrayerTime(method: method, city: city, type: .shuruk, time: values[1]),
      PrayerTime(method: method, city: city, type: .dhohr, time: values[2]),
      PrayerTime(method: method, city: city, type: .asr, time: values[3]),
      PrayerTime(method: method, city: city, type: .maghrib, time: values[4]),
      PrayerTime(method: method, city: city, type: .isha, time: values[5]),
    ]
  }
  
  // Get the next N prayers from given date-time. If the date is not given,
  // current system time will be used as the date used to search from.
  static func getNextPrayers(method: PrayerTimeMethod, city: PrayerTimeCity, count: Int, date: Date = Date()) -> [PrayerTime] {
    var result: [PrayerTime] = []
    var current = date
    while result.count < count {
      result.append(contentsOf: getPrayersForDate(method: method, city: city, date: current).filter { $0.time > date })
      current = Calendar.current.date(byAdding: .day, value: 1, to: current)!
    }
    return Array(result.prefix(count))
  }
  
  // MARK: - Properties
  
  let method: PrayerTimeMethod
  let city: PrayerTimeCity
  let type: PrayerTimeType
  let time: Date

  // MARK: - Computed Properties
  
  // Unique and machine friendly identifier used for matching
  var id: String { "\(method.rawValue)|\(city.rawValue)|\(type)|\(ISO8601DateFormatter().string(from: time))" }

  // Localized time string for the prayer time without the date
  var timeString: String { DateFormatter.localizedString(from: time, dateStyle: .none, timeStyle: .short) }
  
  // Localized time duration from now until the prayer time
  var untilString: String {
    let components = Calendar.current.dateComponents([.hour, .minute], from: Date(), to: time)
    if let hours = components.hour, hours > 0 {
      return String(localized: "prayers_time_until_hm \(hours) \(components.minute ?? 0)")
    }
    return String(localized: "prayers_time_until_m \(components.minute ?? 0)")
  }
  
  // The time when notifications must be shown to the user
  var notifyTime: Date {
    time.addingTimeInterval(-TimeInterval(NotificationOffset.current.seconds))
  }
  
  // Flag to indicate whether the prayer time is "current". A prayer is considered current
  // from the adhan time to 30 minutes from then. This flag is completely arbitrary and only
  // used to emphasize a prayer time immediately from adhan time.
  var isCurrent: Bool {
    let now = Date()
    let end = Calendar.current.date(
      byAdding: .second,
      value: Int(PrayerTime.activeDuration),
      to: now
    )
    if end == nil {
      return false
    }
    return time >= now && time < end!
  }
  
  // Flag to indicate whether the prayer is the "next" prayer. A prayer is considered
  // as next if is the next prayer from current system time. Used to emphasize on UIs.
  var isNext: Bool {
    return PrayerTime.getNextPrayers(method: method, city: city, count: 1).first == self
  }
  
  // Flag to indicate whether the prayer time is in the future or not.
  var isFuture: Bool { time > Date() }
  
  // MARK: - Initializers
  
  // Create a new PrayerTime instance with given values.
  init(method: PrayerTimeMethod, city: PrayerTimeCity, type: PrayerTimeType, time: Date) {
    self.method = method
    self.city = city
    self.type = type
    self.time = time
  }
  
  // Create a new PrayerTime instance by parsing an identifier string. Throws an error
  // if the given string value is not a valid identifier.
  init(id: String) {
    let parts = id.split(separator: "|")
    guard parts.count == 3 else {
      fatalError("Invalid identifier format: \(id)")
    }
    guard let methodRawValue = PrayerTimeMethod(rawValue: String(parts[0])),
          let cityRawValue = PrayerTimeCity(rawValue: String(parts[1])),
          let typeRawValue = PrayerTimeType(rawValue: String(parts[2])),
          let time = ISO8601DateFormatter().date(from: String(parts[3])) else {
      fatalError("Invalid identifier format: \(id)")
    }
    self.method = methodRawValue
    self.city = cityRawValue
    self.type = typeRawValue
    self.time = time
  }
}
