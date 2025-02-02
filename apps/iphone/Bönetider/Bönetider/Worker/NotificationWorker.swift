//
//  NotificationWorker.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-02.
//

import SwiftUI

// NotificationWorker is reponsible for scheduling notifications and
// other tasks related to showing a notification to the user.
struct NotificationWorker {
  static let category = "me.thanish.prayers.se.NotificationWorker"
  
  // Sets up the worker when the app is launched by the user.
  static func initialize() {
    UNUserNotificationCenter.current().setNotificationCategories([
      UNNotificationCategory(
        identifier: category,
        actions: [],
        intentIdentifiers: [],
        options: .customDismissAction
      )
    ])
    NotificationWorker.scheduleTestNotification()
  }
  
  // Schedule a notification for given prayer
  static func schedule(prayer: PrayerTime) {
    guard NotificationOffset.current.enabled else {
      print(">> NotificationWorker: notifications are not enabled")
      return
    }
    guard prayer.city == PrayerTimeCity.current else {
      print (">> NotificationWorker: prayer time has a different city \(prayer.city)")
      return
    }
    print(">> NotificationWorker: scheduling for \(prayer.type) at \(prayer.notifyTime)")
    
    // Prepare notification content
    let content = UNMutableNotificationContent()
    content.title = String(localized: "notification_title \(prayer.type.label)")
    content.body = String(localized: "notification_body \(prayer.timeString)")
    content.sound = .default
    content.categoryIdentifier = category
    
    // Treigger date must be in the future
    guard prayer.isFuture else {
      print(">> NotificationWorker: trigger time is in the past")
      return
    }
    // Create calendar trigger
    let trigger = UNCalendarNotificationTrigger(
      dateMatching: Calendar.current.dateComponents(
        [.year, .month, .day, .hour, .minute],
        from: prayer.notifyTime
      ),
      repeats: false
    )
    // Unique identifier using prayer time ID
    let request = UNNotificationRequest(
      identifier: prayer.id,
      content: content,
      trigger: trigger
    )
    UNUserNotificationCenter.current().add(request) { error in
      if let error = error {
        print(">> NotificationWorker: error scheduling notification: \(error.localizedDescription)")
      }
    }
  }
  
  // Schedule a test notification for debugging purposes.
  static func scheduleTestNotification(delay: TimeInterval = 120) {
    print(">> NotificationWorker: scheduling test notification")
    schedule(prayer: PrayerTime(
      city: PrayerTimeCity.current,
      type: .asr,
      time: Date().addingTimeInterval(delay)
    ))
  }
}
