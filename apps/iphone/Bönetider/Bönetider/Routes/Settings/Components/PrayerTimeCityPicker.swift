//
//  PrayerTimeCityPicker.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-01.
//

import SwiftUI

// PrayerTimeCityPicker is a SwiftUI View for selecting the city
struct PrayerTimeCityPicker: View {
  @Binding var city: PrayerTimeCity
  
  var body: some View {
    List {
      Picker(String(localized: "route_settings_city"), selection: $city) {
        ForEach(PrayerTimeCity.allCases, id: \.self) { city in
          Text(city.label).tag(city)
        }
      }
    }
  }
}
