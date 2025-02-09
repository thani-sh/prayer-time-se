//
//  PrayerTimeData.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//

import Foundation

// PrayerTimeData is data read from bundled JSON files for a specific city.
// Instances of this struct should contain prayer times for the entire year.
struct PrayerTimeData {
  // Cached the recently loaded dataset to avoid reading and parsing the file.
  private static var cache: PrayerTimeData?
  
  // The number of values available per day in bundles JSON file.
  private static let valuesPerDay = 6
  
  // Get the dataset for the city the user has selected (or the default city).
  static var current: PrayerTimeData {
    let method = PrayerTimeMethod.current
    let city = PrayerTimeCity.current
    if cache != nil && cache!.city == city && cache!.method == method {
      return cache!
    }
    let resourceName = method.rawValue + "." + city.rawValue
    guard let url = Bundle.main.url(forResource: resourceName, withExtension: "json"),
          let data = try? Data(contentsOf: url),
          let json = try? JSONSerialization.jsonObject(with: data) as? [[[Int]]] else {
      fatalError("Failed to load prayer time data for \(city.rawValue)")
    }
    let loadedData = PrayerTimeData(method: method, city: city, data: json)
    cache = loadedData
    return loadedData
  }
  
  // MARK: - Properties
  
  let method: PrayerTimeMethod
  let city: PrayerTimeCity
  let data: [[[Int]]]
  
  // MARK: - Methods
  
  // Returns an array of prayer times for the given date. This only contains
  // date values. Use static methods on the PrayerTime struct for a more
  // helpful version. This should not be used directly in almost all cases.
  func forDate(_ date: Date) -> [Date] {
    let calendar = Calendar.current
    let month = calendar.component(.month, from: date) - 1
    let day = calendar.component(.day, from: date) - 1
    let values: [Int] = data[month][day]
    if values.count != PrayerTimeData.valuesPerDay {
      fatalError("Invalid prayer time data for \(city.rawValue): \(values) for \(date)")
    }
    return values.map { minutes in
      var components = calendar.dateComponents([.year, .month, .day], from: date)
      components.hour = minutes / 60
      components.minute = minutes % 60
      return Calendar.current.date(from: components)!
    }
  }
}
