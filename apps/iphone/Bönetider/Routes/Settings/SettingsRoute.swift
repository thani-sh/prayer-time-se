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
  private var notificationOffset: NotificationOffset = NotificationOffset.disabled
  
  @AppStorage(HijriCalendarOffset.key)
  private var calendarOffset: HijriCalendarOffset = HijriCalendarOffset.nothing
  
  @AppStorage(PrayerTimeCity.key)
  private var city: PrayerTimeCity = PrayerTimeCity.standard
  
  @AppStorage(PrayerTimeMethod.key)
  private var method: PrayerTimeMethod = PrayerTimeMethod.standard
  
  @AppStorage(TimeFormat.key)
  private var timeFormat: TimeFormat = TimeFormat.standard

  var body: some View {
    Form {
      Section {
        PrayerTimeCityPicker(city: $city)
        PrayerTimeMethodPicker(method: $method)
        TimeFormatPicker(format: $timeFormat)
      }
      header: { Text(String(localized: "route_settings_section_methodology")) }
      footer: { Text(String(localized: "route_settings_section_methodology_details")) }
      
      Section {
        NotificationEnabledToggle(offset: $notificationOffset)
        NotificationOffsetSlider(offset: $notificationOffset)
      }
      header: { Text(String(localized: "route_settings_section_notifications")) }
      footer: { Text(String(localized: "route_settings_section_notifications_details")) }
      
      Section {
        HijriCalendarOffsetSlider(offset: $calendarOffset)
      }
      header: { Text(String(localized: "route_settings_section_adjustments")) }
      footer: { Text(String(localized: "route_settings_section_adjustments_details")) }
      
      Section {
        RelatedLinksList()
      }
      header: { Text(String(localized: "route_settings_section_development")) }
      footer: { Text(String(localized: "route_settings_section_development_details")) }
    }
  }
}
