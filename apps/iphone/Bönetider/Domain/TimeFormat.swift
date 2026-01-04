//
//  TimeFormat.swift
//  Bönetider
//
//  Created by Jules on 2025-02-18.
//

import SwiftUI

enum TimeFormat: String, CaseIterable, Identifiable {
    case h12 = "h12"
    case h24 = "h24"

    var id: String { rawValue }

    static let key = "TimeFormat"
    static let standard = TimeFormat.h24

    static var current: TimeFormat {
        let storedValue = UserDefaults.standard.string(forKey: key)
        return TimeFormat(rawValue: storedValue ?? "") ?? .standard
    }
}
