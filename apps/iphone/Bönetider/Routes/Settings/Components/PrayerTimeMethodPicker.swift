//
//  PrayerTimeMethodPicker.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-01.
//

import SwiftUI

// PrayerTimeMethodPicker is a SwiftUI View for selecting the methodology
struct PrayerTimeMethodPicker: View {
  @Binding var method: PrayerTimeMethod
  
  var body: some View {
    List {
      Picker(String(localized: "route_settings_method"), selection: $method) {
        ForEach(PrayerTimeMethod.allCases, id: \.self) { method in
          Text(method.label).tag(method)
        }
      }
    }
  }
}
