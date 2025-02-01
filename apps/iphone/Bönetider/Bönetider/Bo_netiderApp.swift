//
//  Bo_netiderApp.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-30.
//

import SwiftUI

@main
struct Bo_netiderApp: App {
  var body: some Scene {
    WindowGroup {
      TabView {
        ForEach(AvailableRoutes.indices, id: \.self) { index in
          let route = AvailableRoutes[index]
          AnyView(route.content).tabItem {
            Image(systemName: route.icon)
            Text(route.text)
          }
        }
      }
    }
  }
}
