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
      await PrayerTimeRepository.shared.syncIfNeeded()
    }
  }

  var body: some Scene {
    WindowGroup {
      Group {
        if ProcessInfo.processInfo.isiOSAppOnMac {
          DesktopApp()
        } else {
          MobileApp()
        }
      }
      .onReceive(NotificationCenter.default.publisher(for: NSNotification.Name("PrayerTimesUpdated"))) { _ in
        SchedulerWorker.initialize()
      }
    }
    .backgroundTask(.appRefresh(SchedulerWorker.identifier)) {
      SchedulerWorker.scheduleNotifications()
    }
  }
}
