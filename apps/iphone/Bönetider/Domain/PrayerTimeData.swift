//
//  PrayerTimeData.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//

import Foundation
import Combine

// PrayerTimeData is data read from bundled JSON files for a specific city.
// Instances of this struct should contain prayer times for the entire year.
struct PrayerTimeData {
  // Cached the recently loaded dataset to avoid reading and parsing the file.
  private static var cache: PrayerTimeData?
  
  // The number of values available per day in bundles JSON file.
  private static let valuesPerDay = 6
  
  static func clearCache() {
    cache = nil
  }

  // Get the dataset for the city the user has selected (or the default city).
  static var current: PrayerTimeData {
    let method = PrayerTimeMethod.current
    let city = PrayerTimeCity.current
    if cache != nil && cache!.city == city && cache!.method == method {
      return cache!
    }
    let resourceName = method.rawValue + "." + city.rawValue
    var targetURL: URL? = nil
    let fileManager = FileManager.default
    if let docDir = fileManager.urls(for: .documentDirectory, in: .userDomainMask).first {
      let docURL = docDir.appendingPathComponent("\(resourceName).json")
      if fileManager.fileExists(atPath: docURL.path) {
        targetURL = docURL
      }
    }
    
    if targetURL == nil {
      targetURL = Bundle.main.url(forResource: resourceName, withExtension: "json")
    }
    
    guard let url = targetURL,
          let data = try? Data(contentsOf: url),
          let json = try? JSONSerialization.jsonObject(with: data) as? [[[Int]]] else {
      fatalError("Failed to load prayer time data for \(city.rawValue)")
    }
    let loadedData = PrayerTimeData(method: method, city: city, data: json)
    cache = loadedData
    return loadedData
  }
  
  // MARK: - Properties
  
  let method: PrayerTimeMethod
  let city: PrayerTimeCity
  let data: [[[Int]]]
  
  // MARK: - Methods
  
  // Returns an array of prayer times for the given date. This only contains
  // date values. Use static methods on the PrayerTime struct for a more
  // helpful version. This should not be used directly in almost all cases.
  func forDate(_ date: Date) -> [Date] {
    let calendar = Calendar.current
    let month = calendar.component(.month, from: date) - 1
    let day = calendar.component(.day, from: date) - 1
    let values: [Int] = data[month][day]
    if values.count != PrayerTimeData.valuesPerDay {
      fatalError("Invalid prayer time data for \(city.rawValue): \(values) for \(date)")
    }
    return values.map { minutes in
      var components = calendar.dateComponents([.year, .month, .day], from: date)
      components.hour = minutes / 60
      components.minute = minutes % 60
      return Calendar.current.date(from: components)!
    }
  }
}

@MainActor
class PrayerTimeRepository: ObservableObject {
  static let shared = PrayerTimeRepository()
  private let baseURL = "https://api.xn--bnetider-n4a.nu/v1"
  private let lastUpdatedKey = "last_updated_version"

  @Published var isSyncing: Bool = false

  func syncIfNeeded(force: Bool = false) async {
    guard !isSyncing else { return }
    
    await MainActor.run {
      self.isSyncing = true
    }
    
    defer {
      Task { @MainActor in
        self.isSyncing = false
      }
    }
    
    do {
      let remoteVersion = try await fetchRemoteVersion()
      let localVersion = UserDefaults.standard.string(forKey: lastUpdatedKey)
      
      if remoteVersion != localVersion || force {
        print(">> PrayerTimeRepository: Syncing prayer times to version \(remoteVersion)")
        try await syncAllPrayerTimes(version: remoteVersion)
      } else {
        print(">> PrayerTimeRepository: Prayer times are up to date (\(remoteVersion))")
      }
    } catch {
      print(">> PrayerTimeRepository: Failed to sync prayer times: \(error)")
    }
  }

  private func fetchRemoteVersion() async throws -> String {
    let url = URL(string: "\(baseURL)/version")!
    let (data, _) = try await URLSession.shared.data(from: url)
    if let json = try JSONSerialization.jsonObject(with: data) as? [String: Any],
       let version = json["updated"] as? String {
      return version
    }
    throw URLError(.cannotParseResponse)
  }

  private func syncAllPrayerTimes(version: String) async throws {
    for method in PrayerTimeMethod.allCases {
      for city in PrayerTimeCity.allCases {
        do {
          try await fetchAndSaveTimes(method: method, city: city)
        } catch {
          print(">> PrayerTimeRepository: Failed to fetch for \(city.rawValue) - \(method.rawValue)")
        }
      }
    }
    UserDefaults.standard.set(version, forKey: lastUpdatedKey)
    PrayerTimeData.clearCache()
    print(">> PrayerTimeRepository: Successfully synced all times.")
    
    await MainActor.run {
      NotificationCenter.default.post(name: NSNotification.Name("PrayerTimesUpdated"), object: nil)
    }
  }

  private func fetchAndSaveTimes(method: PrayerTimeMethod, city: PrayerTimeCity) async throws {
    let urlString = "\(baseURL)/method/\(method.id)/city/\(city.id)/times"
    guard let url = URL(string: urlString) else { return }
    let (data, _) = try await URLSession.shared.data(from: url)
    
    // Validate JSON format matches what we expect
    guard let _ = try JSONSerialization.jsonObject(with: data) as? [[[Int]]] else {
      throw URLError(.cannotParseResponse)
    }
    
    let fileManager = FileManager.default
    let docDir = fileManager.urls(for: .documentDirectory, in: .userDomainMask).first!
    let fileUrl = docDir.appendingPathComponent("\(method.rawValue).\(city.rawValue).json")
    
    try data.write(to: fileUrl, options: .atomic)
  }
}

