//
//  MainRouteHeading.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//

import SwiftUI

struct MainRouteHeading: View {
  let date: Date
  let offset: HijriCalendarOffset
  
  var body: some View {
    VStack(alignment: .center) {
      Text("route_home_title")
        .font(.largeTitle)
      HStack(spacing: 4) {
        Text(getDefaultDateString(date: date))
        Text("|")
        Text(getHijrahDateString(date: date))
      }
      .font(.subheadline)
      .opacity(0.75)
    }
  }
  
  private func getDefaultDateString(date: Date) -> String {
    let formatter = DateFormatter()
    formatter.dateFormat = "d MMM yyyy"
    return formatter.string(from: date)
  }
  
  private func getHijrahDateString(date: Date) -> String {
    let islamicCalendar = Calendar(identifier: .islamic)
    let formatter = DateFormatter()
    formatter.calendar = islamicCalendar
    formatter.locale = Locale(identifier: "ar")
    formatter.dateFormat = "MMM yyyy هـ d"
    return formatter.string(from: offset.apply(date))
  }
}
