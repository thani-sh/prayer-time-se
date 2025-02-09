//
//  HijriCalendarOffset.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-08.
//

import Foundation

// HijriCalendarOffset can be used to fix errors on the hijri calendar.
//
//   @AppStorage(HijriCalendarOffset.key)
//   private var offset: HijriCalendarOffset = HijriCalendarOffset.nothing
//
// To update the stored value, set the HijriCalendarOffset.current value.
//
//   HijriCalendarOffset.current = HijriCalendarOffset.nothing
//   HijriCalendarOffset.current = HijriCalendarOffset(15)
//
struct HijriCalendarOffset: Codable, RawRepresentable, Identifiable, Equatable {
  // Set the raw value type
  typealias RawValue = Int
  
  // Key used to store notification offset on UserDefaults
  static let key: String = "HijriCalendarOffset"
  
  // The default value to use when notifications are toggled on by the user
  static let nothing: HijriCalendarOffset = .init(0)
  
  // Minimum value allowed to be used as the notification offset
  static let min: Int = -7
  
  // Maximum value allowed to be used as the notification offset
  static let max: Int = 7
  
  // The current notification offset value stored in UserDefaults
  static var current : HijriCalendarOffset {
    get { .init(UserDefaults.standard.integer(forKey: key)) }
    set { UserDefaults.standard.set(newValue.days, forKey: key) }
  }
  
  // Calendary date offsets are equal if the number of fays are equal
  static func == (lhs: HijriCalendarOffset, rhs: HijriCalendarOffset) -> Bool {
    lhs.days == rhs.days
  }
  
  // MARK: - Properties
  
  // Offset value in days
  let days: Int
  
  // MARK: - Computed Properties
  
  // Unique and machine friendly identifier used for matching
  var id: String { String(self.days) }
  
  // Used for encoding the struct as a raw value
  var rawValue: Int { days }
  
  // Localized string describing the offset period
  var label: String {
    self == HijriCalendarOffset.nothing
    ? String(localized: "hijri_calendar_offset_disabled")
    : String(localized: "hijri_calendar_offset_template \(days)")
  }
  
  // Creates a struct while ensuring the offset falls between the range
  init(_ days: Int) {
    self.days = days < HijriCalendarOffset.min ? HijriCalendarOffset.min
    : days > HijriCalendarOffset.max ? HijriCalendarOffset.max
    : days
  }
  
  // Create a struct using the raw value representation for decoding
  init(rawValue: Int) {
    self = .init(rawValue)
  }
  
  // apply returns a new date applying the offset
  func apply(_ date: Date) -> Date {
    Calendar.current.date(byAdding: .day, value: self.days, to: date)!
  }
}
