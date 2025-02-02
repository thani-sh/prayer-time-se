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
  private static let notificationCenter = UNUserNotificationCenter.current()
  private static let locationManager = CLLocationManager()
  
  
  // shows the dialog to request permission from the user for notifications
  static func requestNotification(callback: @escaping (Bool) -> Void) {
    notificationCenter.requestAuthorization(options: [.alert, .sound]) { granted, _ in callback(granted) }
  }
  
  // shows the dialog to request permission from the user for user location
  static func requestLocation(callback: @escaping (Bool) -> Void) {
    locationManager.requestWhenInUseAuthorization()
    callback(locationManager.authorizationStatus == .authorizedWhenInUse || locationManager.authorizationStatus == .authorizedAlways)
  }
}
