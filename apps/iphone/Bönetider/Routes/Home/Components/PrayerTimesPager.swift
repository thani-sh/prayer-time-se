//
//  MainRouteContent.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//


import SwiftUI

struct PrayerTimesPager: View {
  // MARK: - Properties

  @Binding
  var date: Date
  var method: PrayerTimeMethod
  var city: PrayerTimeCity

  @State
  private var currentPage = 0

  // MARK: - Computed Properties

  private var today: Date {
    Calendar.current.startOfDay(for: Date())
  }

  private var startDate: Date {
    Calendar.current.date(byAdding: .day, value: -7, to: today)!
  }

  private var totalPages: Int {
    35
  }

  // MARK: - Methods

  private func daysFromStartDate(date: Date) -> Int {
    Calendar.current.dateComponents([.day], from: startDate, to: date).day ?? 0
  }

  // MARK: - View

  var body: some View {
    TabView(selection: Binding(
      get: { self.currentPage },
      set: {
        self.currentPage = $0;
        self.date = Calendar.current.date(byAdding: .day, value: $0, to: startDate)!
      }
    )) {
      ForEach(0..<totalPages, id: \.self) { page in
        let pageDate = Calendar.current.date(byAdding: .day, value: page, to: startDate)!
        PrayerTimesDayView(method: method, city: city, date: pageDate).tag(page)
      }
    }
    .frame(height: 480)
    .tabViewStyle(.page(indexDisplayMode: .never))
    .onAppear { currentPage = daysFromStartDate(date: date) }
  }
}
