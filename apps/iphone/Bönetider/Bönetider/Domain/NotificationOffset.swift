//
//  NotificationOffset.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//

import Foundation

// NotificationOffset is the time gap before the notification before adhan.
// This value van be used on SwiftUI views using the snippet given below.
//
//   @AppStorage(NotificationOffset.key)
//   private var offset: NotificationOffset = NotificationOffset.disabled
//
// To update the stored value, set the NotificationOffset.current value.
//
//   NotificationOffset.current = NotificationOffset.disabled
//   NotificationOffset.current = NotificationOffset(15)
//
struct NotificationOffset: Codable, RawRepresentable, Identifiable, Equatable {
  // Set the raw value type
  typealias RawValue = Int

  // Key used to store notification offset on UserDefaults
  static let key: String = "NotificationOffset"
  
  // This special value is used to indicate when notifications are disabled
  static let disabled: NotificationOffset = .init(-1)
  
  // The default value to use when notifications are toggled on by the user
  static let standard: NotificationOffset = .init(5)
  
  // Minimum value allowed to be used as the notification offset
  static let min: Int = 0
  
  // Maximum value allowed to be used as the notification offset
  static let max: Int = 30
  
  // The current notification offset value stored in UserDefaults
  static var current : NotificationOffset {
    get { .init(UserDefaults.standard.integer(forKey: key)) }
    set { UserDefaults.standard.set(newValue.minutes, forKey: key) }
  }
  
  // Notification offsets are equal if the number of mintues are equal
  static func == (lhs: NotificationOffset, rhs: NotificationOffset) -> Bool {
    lhs.minutes == rhs.minutes
  }
  
  // MARK: - Properties
  
  // Offset value in minutes before adhan time
  let minutes: Int
  
  // MARK: - Computed Properties
  
  // Unique and machine friendly identifier used for matching
  var id: String { String(self.minutes) }
  
  // Used for encoding the struct as a raw value
  var rawValue: Int { minutes }
  
  // Offset value in seconds before adhan time
  var seconds: Int { minutes * 60 }
  
  // Indicates whether notifications are enabled or disabled
  var enabled: Bool { self != NotificationOffset.disabled }
  
  // Localized string describing the offset period
  var label: String {
    minutes == NotificationOffset.disabled.minutes
    ? String(localized: "notification_offset_disabled")
    : String(localized: "notification_offset_template \(minutes)")
  }
  
  // Creates a struct while ensuring the offset falls between the range
  init(_ minutes: Int) {
    self.minutes = minutes < NotificationOffset.min ? NotificationOffset.min
    : minutes > NotificationOffset.max ? NotificationOffset.max
    : minutes
  }
  
  // Create a struct using the raw value representation for decoding
  init(rawValue: Int) {
    self = .init(rawValue)
  }
  
  // Creates a struct based on whether notifications are enabled or disabled
  init(enabled: Bool) {
    self.minutes = enabled
    ? NotificationOffset.standard.minutes
    : NotificationOffset.disabled.minutes
  }
}
