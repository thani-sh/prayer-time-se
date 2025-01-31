//
//  SettingsRoute.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-30.
//

import SwiftUI

struct SettingsRouteSpec: RouteSpec {
  let name: String      = "settings"
  let text: String      = String(localized: "route_settings_name")
  let type: RouteType   = .PRIMARY
  let icon: String      = "gearshape"
  let content: any View = SettingsRoute()
}

struct SettingsRoute: View {
    var body: some View {
      NavigationView {
        Form {
          Section {
            PrayerTimeCityPicker()
            PrayerTimeMethodPicker()
          }
            header: { Text(String(localized: "route_settings_section_methodology")) }
            footer: { Text(String(localized: "prayers_method_islamiskaforbundet_details")) }
        }
      }
      .navigationTitle(String(localized: "route_settings_name"))
      .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    SettingsRoute()
}
