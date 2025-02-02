//
//  Bo_netiderApp.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-30.
//

import SwiftUI

@main
struct Bo_netiderApp: App {
  // Schedules notifications and the next refresh
  init() {
    Task {
      NotificationWorker.initialize()
      SchedulerWorker.initialize()
    }
  }

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
    .backgroundTask(.appRefresh(SchedulerWorker.identifier)) {
      SchedulerWorker.scheduleNotifications()
    }
  }
}
