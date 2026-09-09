//
//  NotificationEnabledToggle.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-01.
//

import SwiftUI

// NotificationEnabledToggle is a SwiftUI View for selecting the delay
struct NotificationEnabledToggle: View {
  @Binding
  var offset: NotificationOffset
  
  // Handles the master notifications toggle. Enabling requests permission
  // and reschedules; disabling purges the pending queue via the scheduler.
  func handleToggleChange(enabled: Bool) {
    if enabled {
      Permissions.requestNotificationPermission({ success in
        if success {
          SchedulerWorker.scheduleNotifications()
        } else {
          offset = NotificationOffset.disabled
        }
      })
    } else {
      SchedulerWorker.scheduleNotifications()
    }
  }
  
  var body: some View {
    List {
      Toggle(isOn: Binding(
        get: { offset.enabled },
        set: { offset = .init(enabled: $0) }
      )) {
        Text(String(localized: "notification_before_adhan_enabled"))
      }
    }
    .onChange(of: offset.enabled) { _, enabled in handleToggleChange(enabled: enabled) }
  }
}
