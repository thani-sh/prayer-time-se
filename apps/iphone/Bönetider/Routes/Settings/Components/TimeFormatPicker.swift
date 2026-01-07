//
//  TimeFormatPicker.swift
//  PrayerTimes
//
//  Created by Thanish Nizam on 2025-02-01.
//

import SwiftUI

struct TimeFormatPicker: View {
  @Binding
  var timeFormat: TimeFormat

  var body: some View {
    Picker(String(localized: "route_settings_time_format"), selection: $timeFormat) {
      ForEach(TimeFormat.allCases) { timeFormat in
        Text(timeFormat.label).tag(timeFormat)
      }
    }
  }
}
