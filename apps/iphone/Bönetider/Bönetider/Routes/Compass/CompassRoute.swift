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
  let type: RouteType   = .PRIMARY
  let icon: String      = "location.north.circle"
  let content: any View = CompassRoute()
}

struct CompassRoute: View {
    var body: some View {
      Text(String(localized: "shared_coming_soon"))
    }
}

#Preview {
    CompassRoute()
}
