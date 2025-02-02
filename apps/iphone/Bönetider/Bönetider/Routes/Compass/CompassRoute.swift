//
//  CompassRoute.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-30.
//

import SwiftUI

struct CompassRouteSpec: RouteSpec {
  let name: String      = "compass"
  let text: String      = String(localized: "route_compass_name")
  let icon: String      = "arrow.up.circle.dotted"
  let content: any View = CompassRoute()
}

struct CompassRoute: View {
  var body: some View {
    VStack {
      Text(String(localized: "shared_coming_soon"))
    }
    .onAppear {
      Permissions.requestLocation({ _ in })
    }
  }
}
