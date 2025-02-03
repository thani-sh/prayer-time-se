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
  @StateObject
  private var locationService = LocationService()
  
  @State
  private var qibla: Double = 0
  
  @State
  private var heading: Double = 0
  
  @State
  private var accuracy = LocationService.HeadingAccuracy.unknown
  
  var body: some View {
    QiblaCompass(qibla: qibla, heading: heading, accuracy: accuracy)
      .onAppear {
        locationService.start({ newQibla, _, newHeading, newAccuracy in
          withAnimation {
            qibla = newQibla
            heading = newHeading
            accuracy = newAccuracy
          }
        })
      }
      .onDisappear {
        locationService.stop()
      }
  }
}
