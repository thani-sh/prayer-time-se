//
//  Permissions.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-01.
//

import Foundation
import UserNotifications

// Permissions need to be obtained from the user using the app
struct Permissions {
  // request shows the dialog to request permission from the user
  static func requestNotification(callback: @escaping (Bool) -> Void) {
    UNUserNotificationCenter
      .current()
      .requestAuthorization(options: [.alert, .sound]) { granted, _ in callback(granted) }
  }
}
