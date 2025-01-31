//
//  RouteSpec.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-30.
//

import Foundation
import SwiftUICore

// RouteType describes the type of route. This is used to decide
// whether to show them on the main navigation bar or not.
enum RouteType {
  case PRIMARY
  case SECONDARY
}

// RouteSpec describes a route
protocol RouteSpec {
  var name: String { get }
  var text: String { get }
  var type: RouteType { get }
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
