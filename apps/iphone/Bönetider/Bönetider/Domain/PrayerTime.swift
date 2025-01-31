//
//  PrayerTime.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//


import SwiftUI

struct PrayerTime: Identifiable {
    let city: PrayerTimeCity
    let type: PrayerTimeType
    let time: Date
    
    // MARK: - Identifiable
    var id: String { stringId }
    
    // MARK: - Computed Properties
    var intId: Int { stringId.hashValue }
    var stringId: String { "\(city.rawValue)|\(type)|\(time.iso8601String)" }
    var epochSeconds: Int { Int(time.timeIntervalSince1970) }
    var epochMilli: Int64 { Int64(time.timeIntervalSince1970 * 1000) }
    var timeString: String {
        let formatter = DateFormatter()
        formatter.timeStyle = .short
        return formatter.string(from: time)
    }
    
    // MARK: - Prayer Time Status
    func untilString() -> LocalizedStringKey {
        let now = Date()
        let components = Calendar.current.dateComponents([.hour, .minute], 
                                                         from: now,
                                                         to: time)
        
        if let hours = components.hour, hours > 0 {
            return "prayers_time_until_hm \(hours) \(components.minute ?? 0)"
        }
        return "prayers_time_until_m \(components.minute ?? 0)"
    }
    
    func isCurrentPrayer() -> Bool {
        let calendar = Calendar.current
        guard let endTime = calendar.date(byAdding: .minute, 
                                         value: Int(Self.activeDurationMinutes),
                                         to: time) else {
            return false
        }
        return Date() >= time && Date() < endTime
    }
    
    func isNextPrayer() -> Bool {
      return Self.getNext(city: city, count: 1).first?.id == self.id
    }
    
    // MARK: - Static Properties/Methods
    static let activeDurationMinutes: Double = 30
    
    static func from(stringId: String) -> PrayerTime? {
        let components = stringId.components(separatedBy: "|")
        guard components.count == 3,
              let city = PrayerTimeCity(rawValue: components[0]),
              let type = PrayerTimeType(rawValue: components[1]),
              let date = Date(iso8601String: components[2]) else {
            return nil
        }
        return PrayerTime(city: city, type: type, time: date)
    }
    
    static func getNextPrayer(city: PrayerTimeCity) -> PrayerTime {
        guard let next = getNext(city: city, count: 1).first else {
            fatalError("No prayer times found for \(city.rawValue)")
        }
        return next
    }
    
    static func getNext(city: PrayerTimeCity, 
                       count: Int, 
                       from date: Date = Date()) -> [PrayerTime] {
        var result = [PrayerTime]()
        var currentDate = date
        var daysChecked = 0
        
        while result.count < count && daysChecked < 365 {
            let prayers = PrayerTimeTable.forDate(city: city, date: currentDate)
            
            for prayer in prayers.prayerTimes {
                if prayer.time > date && result.count < count {
                    result.append(prayer)
                }
            }
            
            currentDate = Calendar.current.startOfNextDay(for: currentDate)
            daysChecked += 1
        }
        
        return Array(result.prefix(count))
    }
}

// MARK: - Date Helpers
extension Date {
    var iso8601String: String {
        ISO8601DateFormatter().string(from: self)
    }
    
    init?(iso8601String: String) {
        guard let date = ISO8601DateFormatter().date(from: iso8601String) else {
            return nil
        }
        self = date
    }
}

extension Calendar {
    func startOfNextDay(for date: Date) -> Date {
        let nextDay = self.date(byAdding: .day, value: 1, to: date)!
        return self.startOfDay(for: nextDay)
    }
}
