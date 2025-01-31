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
  let type: RouteType   = .PRIMARY
  let icon: String      = "calendar"
  let content: any View = HomeRoute()
}

struct HomeRoute: View {
    @State var currentDate: Date = Date()
    @AppStorage("PrayerTimeCity") private var selectedCity: String = PrayerTimeCity.stockholm.rawValue
  
    var body: some View {
      VStack(alignment: .center) {
        MainRouteHeading(date: currentDate)
        MainRouteContent(city: PrayerTimeCity(rawValue: selectedCity)!, currentDate: $currentDate)
      }
    }
}

#Preview {
    HomeRoute()
}
