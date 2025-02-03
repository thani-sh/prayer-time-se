//
//  Permissions.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-01.
//

import Foundation
import UserNotifications
import CoreLocation

// Permissions need to be obtained from the user using the app
struct Permissions {
  // shows the dialog to request permission from the user for notifications
  static func requestNotificationPermission(_ callback: @escaping (Bool) -> Void) {
    let notificationCenter = UNUserNotificationCenter.current()
    notificationCenter.requestAuthorization(options: [.alert, .sound]) { granted, _ in callback(granted) }
  }
  
  // shows the dialog to request permission from the user for user location
  static func requestLocationPermission() {
    let locationManager = CLLocationManager()
    locationManager.requestWhenInUseAuthorization()
  }
}
