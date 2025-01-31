//
//  NotificationOffset.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//


import Foundation
import SwiftUI

struct NotificationOffset: Equatable {
    let minutes: Int
    
    static let disabledOffset = -1
    private static let storeKey = "NotificationOffset"
    
    var label: LocalizedStringKey {
        if minutes == Self.disabledOffset {
            return "notification_offset_disabled"
        }
        return "notification_offset_template \(minutes)"
    }
    
    var milliseconds: Int {
        minutes * 60 * 1000
    }
    
    static var entries: [NotificationOffset] {
        [
            NotificationOffset(minutes: disabledOffset),
            NotificationOffset(minutes: 10),
            NotificationOffset(minutes: 15),
            NotificationOffset(minutes: 20)
        ]
    }
    
    // MARK: - Storage Management
    static func set(_ offset: NotificationOffset) {
        UserDefaults.standard.set(offset.minutes, forKey: storeKey)
    }
    
    static func get() -> NotificationOffset {
        let storedValue = UserDefaults.standard.integer(forKey: storeKey)
        return NotificationOffset(minutes: storedValue)
    }
    
    static func isEnabled() -> Bool {
        return get().minutes != disabledOffset
    }
}
