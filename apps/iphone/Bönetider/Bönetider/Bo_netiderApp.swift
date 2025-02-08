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
      if ProcessInfo.processInfo.isiOSAppOnMac {
        DesktopApp()
      } else {
        MobileApp()
      }
    }
    .backgroundTask(.appRefresh(SchedulerWorker.identifier)) {
      SchedulerWorker.scheduleNotifications()
    }
  }
}
