//
//  PrayerTimeMethod.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//

import SwiftUI

// PrayerTimeMethod is the city the prayer time is calculated for
enum PrayerTimeMethod: String, CaseIterable, Identifiable {
  case islamiskaforbundet
  
  var id: String { self.rawValue }
  
  /// Get localized city name
  func getLabel() -> String {
    NSLocalizedString("prayers_method_" + self.rawValue, comment: "")
  }
}

// PrayerTimeMethodPicker is a SwiftUI View for selecting the methodology
struct PrayerTimeMethodPicker: View {
  @AppStorage("PrayerTimeMethod") private var selectedMethod: String = PrayerTimeMethod.islamiskaforbundet.rawValue
  
  var body: some View {
    List {
      Picker(String(localized: "route_settings_method"), selection: Binding(
        get: { PrayerTimeMethod(rawValue: selectedMethod) ?? .islamiskaforbundet },
        set: { selectedMethod = $0.rawValue }
      )) {
        ForEach(PrayerTimeMethod.allCases, id: \.self) { method in
          Text(method.getLabel()).tag(method)
        }
      }
    }
  }
}

struct PrayerTimeMethodPicker_Previews: PreviewProvider {
  static var previews: some View {
    PrayerTimeMethodPicker()
  }
}
