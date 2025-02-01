//
//  HomeRoute.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-30.
//

import SwiftUI

struct HomeRouteSpec: RouteSpec {
  let name: String      = "home"
  let text: String      = String(localized: "route_home_name")
  let icon: String      = "calendar.badge.clock"
  let content: any View = HomeRoute()
}

struct HomeRoute: View {
  @State
  var currentDate: Date = Date()
  
  @AppStorage(PrayerTimeCity.key)
  private var city: PrayerTimeCity = PrayerTimeCity.standard
  
  var body: some View {
    VStack(alignment: .center) {
      MainRouteHeading(date: currentDate)
      PrayerTimesPager(date: $currentDate, city: city)
    }
  }
}
