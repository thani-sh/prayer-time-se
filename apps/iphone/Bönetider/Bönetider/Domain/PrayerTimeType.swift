//
//  PrayerTimeType.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//


import SwiftUI

/**
 PrayerTimeType is the type of a prayer time listed in the app.
 Note: This also includes "sunrise" which is not a prayer time.
 */
enum PrayerTimeType: String, CaseIterable, Identifiable {
    case fajr
    case shuruk
    case dhohr
    case asr
    case maghrib
    case isha
  
    var id: String { self.rawValue }
    
    /// Returns the localized display name for the prayer time
    var label: LocalizedStringKey {
        switch self {
        case .fajr: return "prayers_type_fajr"
        case .shuruk: return "prayers_type_shuruk"
        case .dhohr: return "prayers_type_dhohr"
        case .asr: return "prayers_type_asr"
        case .maghrib: return "prayers_type_maghrib"
        case .isha: return "prayers_type_isha"
        }
    }
    
    /// Indicates whether notifications should be sent for this time
    var shouldNotify: Bool {
        self != .shuruk
    }
}
