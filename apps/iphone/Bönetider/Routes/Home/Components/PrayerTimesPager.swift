//
//  MainRouteContent.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//


import SwiftUI

struct PrayerTimesPager: View {
  private static let initial = Calendar.current.date(from: DateComponents(year: 2024, month: 12, day: 1))!
  private static let maximum = Calendar.current.date(from: DateComponents(year: 2025, month: 12, day: 31))!
  
  // MARK: - Properties
  
  @Binding
  var date: Date
  var method: PrayerTimeMethod
  var city: PrayerTimeCity
  
  @State
  private var currentPage = 0
  
  // MARK: - Computed Properties
  
  private var totalPages: Int {
    Calendar.current.dateComponents([.day], from: Self.initial, to: Self.maximum).day!
  }
  
  // MARK: - Methods
  
  private func daysFromInitial(date: Date) -> Int {
    Calendar.current.dateComponents([.day], from: Self.initial, to: date).day ?? 0
  }
  
  // MARK: - View
  
  var body: some View {
    TabView(selection: Binding(
      get: { self.currentPage },
      set: {
        self.currentPage = $0;
        self.date = Calendar.current.date(byAdding: .day, value: $0, to: Self.initial)!
      }
    )) {
      ForEach(0..<totalPages, id: \.self) { page in
        let pageDate = Calendar.current.date(byAdding: .day, value: page, to: Self.initial)!
        PrayerTimesDayView(method: method, city: city, date: pageDate).tag(page)
      }
    }
    .frame(height: 480)
    .tabViewStyle(.page(indexDisplayMode: .never))
    .onAppear { currentPage = daysFromInitial(date: date) }
  }
}
