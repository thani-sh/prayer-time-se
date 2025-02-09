//
//  PrayerTimeType.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//

import SwiftUI

// PrayerTimeType is the type of a prayer time listed in the app.
// Note: This also includes "sunrise" which is not a prayer time.
enum PrayerTimeType: String, CaseIterable, Identifiable {
  case fajr
  case shuruk
  case dhohr
  case asr
  case maghrib
  case isha
  
  // MARK: - Computed Properties
  
  // Unique and machine friendly identifier used for matching
  var id: String { self.rawValue }
  
  // Indicates whether notifications should be sent for this time
  var notifiable: Bool { self != .shuruk }
  
  // Localized string of the prayer time cuty
  var label: String {
    switch self {
      case .fajr:
        return String(localized: "prayers_type_fajr")
      case .shuruk:
        return String(localized: "prayers_type_shuruk")
      case .dhohr:
        return String(localized: "prayers_type_dhohr")
      case .asr:
        return String(localized: "prayers_type_asr")
      case .maghrib:
        return String(localized: "prayers_type_maghrib")
      case .isha:
        return String(localized: "prayers_type_isha")
    }
  }
}
