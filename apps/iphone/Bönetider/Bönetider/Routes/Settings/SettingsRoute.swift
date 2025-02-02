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
  let icon: String      = "gear"
  let content: any View = SettingsRoute()
}

struct SettingsRoute: View {
  @AppStorage(NotificationOffset.key)
  private var offset: NotificationOffset = NotificationOffset.disabled
  
  @AppStorage(PrayerTimeCity.key)
  private var city: PrayerTimeCity = PrayerTimeCity.standard
  
  @AppStorage(PrayerTimeMethod.key)
  private var method: PrayerTimeMethod = PrayerTimeMethod.standard
  
  func enableNotifications() {
    Permissions.requestNotification({ success in
      if success {
        SchedulerWorker.scheduleNotifications()
      } else {
        offset = NotificationOffset.disabled
      }
    })
  }
  
  var body: some View {
    NavigationView {
      Form {
        Section {
          PrayerTimeCityPicker(city: $city)
          PrayerTimeMethodPicker(method: $method)
        }
          header: { Text(String(localized: "route_settings_section_methodology")) }
          footer: { Text(String(localized: "prayers_method_islamiskaforbundet_details")) }
        
        Section {
          NotificationEnabledToggle(offset: $offset)
            .onChange(of: offset) { enableNotifications() }
          NotificationOffsetSlider(offset: $offset)
        }
          header: { Text(String(localized: "route_settings_section_notifications")) }
          footer: { Text(String(localized: "route_settings_section_notifications_details")) }
        
        Section {
          OpenGithubRepositoryLink()
        }
          header: { Text(String(localized: "route_settings_section_development")) }
          footer: { Text(String(localized: "route_settings_section_development_details")) }
      }
    }
    .navigationTitle(String(localized: "route_settings_name"))
    .navigationBarTitleDisplayMode(.inline)
  }
}
