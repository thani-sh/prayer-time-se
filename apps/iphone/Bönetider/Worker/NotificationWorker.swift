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
  }
  
  // Schedule notifications (exact and pre-Adhan) for given prayer
  static func schedule(prayer: PrayerTime) {
    guard prayer.city == PrayerTimeCity.current else {
      print(">> NotificationWorker: prayer time has a different city \(prayer.city)")
      return
    }
    
    // 1. Schedule Exact On-Time Notification
    scheduleNotification(
      prayer: prayer,
      triggerDate: prayer.time,
      identifier: "\(prayer.id)-exact",
      body: String(localized: "notification_on_time_body \(prayer.type.label)")
    )
    
    // 2. Schedule Pre-Adhan Notification (if enabled and offset > 0)
    if NotificationOffset.current.enabled && prayer.notifyTime < prayer.time {
      scheduleNotification(
        prayer: prayer,
        triggerDate: prayer.notifyTime,
        identifier: "\(prayer.id)-preadhan",
        body: String(localized: "notification_body \(prayer.type.label) \(prayer.timeString)")
      )
    }
  }

  private static func scheduleNotification(
    prayer: PrayerTime,
    triggerDate: Date,
    identifier: String,
    body: String
  ) {
    guard triggerDate > Date() else {
      print(">> NotificationWorker: trigger time \(triggerDate) is in the past")
      return
    }
    
    print(">> NotificationWorker: scheduling [\(identifier)] for \(prayer.type) at \(triggerDate)")
    
    let content = UNMutableNotificationContent()
    content.title = String(localized: "notification_title \(prayer.type.label)")
    content.body = body
    content.sound = .default
    content.categoryIdentifier = category
    
    let trigger = UNCalendarNotificationTrigger(
      dateMatching: Calendar.current.dateComponents(
        [.year, .month, .day, .hour, .minute],
        from: triggerDate
      ),
      repeats: false
    )
    
    let request = UNNotificationRequest(
      identifier: identifier,
      content: content,
      trigger: trigger
    )
    
    UNUserNotificationCenter.current().add(request) { error in
      if let error = error {
        print(">> NotificationWorker: error scheduling notification [\(identifier)]: \(error.localizedDescription)")
      }
    }
  }
}
