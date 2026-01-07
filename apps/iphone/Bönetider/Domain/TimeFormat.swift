//
//  TimeFormat.swift
//  PrayerTimes
//
//  Created by Thanish Nizam on 2025-02-01.
//

import SwiftUI

// TimeFormat is the format of the time.
// This value can be used on SwiftUI views using the snippet given below.
//
//   @AppStorage(TimeFormat.key)
//   private var timeFormat: TimeFormat = TimeFormat.standard
//
// To update the stored value, set the TimeFormat.current value.
//
//   TimeFormat.current = TimeFormat.h24
//
enum TimeFormat: String, CaseIterable, Identifiable {
  case h12, h24

  // Key used to store time format on UserDefaults
  static let key: String = "TimeFormat"

  // The default value to use before a value is picked by the user
  static let standard: TimeFormat = .h24

  // The current time format value stored in UserDefaults
  static var current : TimeFormat {
    get {
      let storedValue = UserDefaults.standard.string(forKey: key)
      return Self(rawValue: storedValue ?? "") ?? TimeFormat.standard
    }
    set { UserDefaults.standard.set(newValue.id, forKey: key) }
  }

  // MARK: - Computed Properties

  // Unique and machine friendly identifier used for matching
  var id: String { self.rawValue }

  // Localized string of the time format
  var label: String {
    switch self {
      case .h12:
        return String(localized: "time_format_12h")
      case .h24:
        return String(localized: "time_format_24h")
    }
  }
}
