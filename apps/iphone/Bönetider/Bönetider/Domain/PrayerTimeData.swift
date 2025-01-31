//
//  PrayerTimeData.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//


import Foundation

/**
 PrayerTimeData contains data read from asset json files.
 */
struct PrayerTimeData {
    let city: PrayerTimeCity
    private let data: [[[Int]]]
    
    private enum PrayerTimeIndex: Int {
        case fajr = 0
        case shuruk = 1
        case dhohr = 2
        case asrShafi = 3
        case asrHanafi = 4
        case maghrib = 5
        case isha = 6
    }
    
    // MARK: - Prayer Time Getters
    
    func fajrTime(for date: Date) -> Date {
        return prayerTime(for: date, index: .fajr)
    }
    
    func shurukTime(for date: Date) -> Date {
        return prayerTime(for: date, index: .shuruk)
    }
    
    func dhohrTime(for date: Date) -> Date {
        return prayerTime(for: date, index: .dhohr)
    }
    
    func asrTime(for date: Date) -> Date {
        return prayerTime(for: date, index: .asrShafi)
    }
    
    func maghribTime(for date: Date) -> Date {
        return prayerTime(for: date, index: .maghrib)
    }
    
    func ishaTime(for date: Date) -> Date {
        return prayerTime(for: date, index: .isha)
    }
    
    // MARK: - Private Helpers
    
    private func prayerTime(for date: Date, index: PrayerTimeIndex) -> Date {
        let calendar = Calendar.current
        let month = calendar.component(.month, from: date) - 1
        let day = calendar.component(.day, from: date) - 1
        let minutes = data[month][day][index.rawValue]
        
        var components = calendar.dateComponents([.year, .month, .day], from: date)
        components.hour = minutes / 60
        components.minute = minutes % 60
        
        return calendar.date(from: components) ?? date
    }
    
    // MARK: - Data Loading
    
    private static var current: PrayerTimeData?
    
    static func get(for city: PrayerTimeCity) -> PrayerTimeData {
        if let current = current, current.city == city {
            return current
        }
        
        let newData = loadData(for: city)
        current = loadData(for: city)
        return newData
    }
    
    private static func loadData(for city: PrayerTimeCity) -> PrayerTimeData {
        guard let url = Bundle.main.url(forResource: city.rawValue, withExtension: "json"),
              let data = try? Data(contentsOf: url),
              let jsonData = try? JSONSerialization.jsonObject(with: data) as? [[[Int]]]
        else {
            fatalError("Failed to load prayer time data for \(city.rawValue)")
        }
        return PrayerTimeData(city: city, data: jsonData)
    }
}
