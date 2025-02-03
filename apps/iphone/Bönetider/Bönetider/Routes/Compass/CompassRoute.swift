//
//  CompassRoute.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-30.
//

import SwiftUI
import CoreMotion
import CoreLocation
import CoreLocationUI

struct CompassRouteSpec: RouteSpec {
  let name: String      = "compass"
  let text: String      = String(localized: "route_compass_name")
  let icon: String      = "arrow.up.circle.dotted"
  let content: any View = CompassRoute()
}

struct CompassRoute: View {
  @State
  private var locationService = LocationService()
  
  @State
  private var locationPermissionGranted: Bool = false
  
  var body: some View {
    ZStack {
      VStack {
        QiblaCompass(
          qibla: locationService.qibla,
          heading: locationService.heading,
          accuracy: locationService.accuracy
        )
        .onAppear { locationService.start() }
        .onDisappear { locationService.stop() }
      }
      
      if !locationPermissionGranted {
        Rectangle()
          .fill(.white)
          .opacity(0.95)
        LocationButton(.currentLocation) { locationPermissionGranted = true }
          .cornerRadius(32)
          .symbolVariant(.fill)
          .labelStyle(.titleAndIcon)
          .foregroundColor(Color.white)
      }
    }
  }
}
