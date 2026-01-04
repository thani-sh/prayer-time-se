//
//  TimeFormatPicker.swift
//  Bönetider
//
//  Created by Jules on 2025-02-18.
//

import SwiftUI

struct TimeFormatPicker: View {
  @Binding var format: TimeFormat

  var body: some View {
    Picker(String(localized: "route_settings_time_format"), selection: $format) {
      ForEach(TimeFormat.allCases) { option in
        Text(String(localized: "time_format_\(option.rawValue)")).tag(option)
      }
    }
    .pickerStyle(.navigationLink)
  }
}

#Preview {
  List {
    TimeFormatPicker(format: .constant(.h24))
  }
}
