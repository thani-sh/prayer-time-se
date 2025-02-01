//
//  RouteSpec.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-30.
//

import SwiftUI

// RouteSpec describes a route
protocol RouteSpec {
  var name: String { get }
  var text: String { get }
  var icon: String { get }
  var content: any View { get }
}

// AvailableRoutes is a list of all routes used by the application.
var AvailableRoutes: [RouteSpec] = [
  HomeRouteSpec(),
  CompassRouteSpec(),
  SettingsRouteSpec()
]

// DefaultRoute is the route that will be shown when the app is opened.
var DefaultRoute: RouteSpec {
  AvailableRoutes.first!
}
