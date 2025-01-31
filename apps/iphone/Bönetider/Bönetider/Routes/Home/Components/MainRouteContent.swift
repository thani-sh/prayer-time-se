//
//  MainRouteContent.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-01-31.
//


import SwiftUI

struct MainRouteContent: View {
    let city: PrayerTimeCity
    @Binding var currentDate: Date
    
    // Date range constants
    private static let initialPageDate = Calendar.current.date(from: DateComponents(year: 2024, month: 12, day: 1))!
    private static let maximumPageDate = Calendar.current.date(from: DateComponents(year: 2025, month: 12, day: 31))!
    
    @State private var currentPage = 0
  
    private var totalPages: Int {
        Calendar.current.dateComponents([.day], from: Self.initialPageDate, to: Self.maximumPageDate).day!
    }
    
    var body: some View {
      TabView(selection: Binding(
        get: { self.currentPage },
        set: { self.currentPage = $0; self.currentDate = Calendar.current.date(byAdding: .day, value: $0, to: Self.initialPageDate)! }
      )) {
            ForEach(0..<totalPages, id: \.self) { page in
                let date = Calendar.current.date(byAdding: .day, value: page, to: Self.initialPageDate)!
                PrayerTimesDayView(city: city, date: date).tag(page)
            }
        }
        .frame(height: 400)
        .tabViewStyle(.page(indexDisplayMode: .never))
        .onAppear {
            currentPage = daysFromInitial(date: currentDate)
        }
    }
    
    private func daysFromInitial(date: Date) -> Int {
        Calendar.current.dateComponents([.day], from: Self.initialPageDate, to: date).day ?? 0
    }
}

struct PrayerTimesDayView: View {
    let city: PrayerTimeCity
    let date: Date
    
    var body: some View {
        VStack(spacing: 0) {
          let prayerTable = PrayerTimeTable.forDate(city: city, date: date)
          ForEach(prayerTable.prayerTimes.indices, id: \.self) { index in
              let prayerTime = prayerTable.prayerTimes[index]
              
              VStack(spacing: 0) {
                  if index != 0 {
                      FadingHorizontalDivider()
                          .padding(.vertical, 8)
                  }
                  
                  PrayerTimeRow(prayerTime: prayerTime)
                      .padding(.vertical, 12)
              }
          }
        }
        .padding(.horizontal)
    }
}

struct PrayerTimeRow: View {
    let prayerTime: PrayerTime
    
    var body: some View {
      HStack (spacing: 24) {
            Text(prayerTime.type.label)
                .font(.subheadline.weight(.medium))
                .foregroundStyle(textColor)
                .frame(maxWidth: .infinity, alignment: .trailing)
            
            Text(prayerTime.timeString)
                .font(.subheadline)
                .foregroundStyle(textColor)
                .frame(maxWidth: .infinity, alignment: .leading)
        }
    }
    
    private var textColor: Color {
        if prayerTime.isCurrentPrayer() {
            return .primary
        } else if prayerTime.isNextPrayer() {
            return .primary.opacity(0.8)
        } else {
            return .primary.opacity(0.6)
        }
    }
}

struct FadingHorizontalDivider: View {
    var body: some View {
        Rectangle()
            .fill(LinearGradient(
                gradient: Gradient(stops: [
                    .init(color: .clear, location: 0),
                    .init(color: .primary.opacity(0.1), location: 0.5),
                    .init(color: .clear, location: 1)
                ]),
                startPoint: .leading,
                endPoint: .trailing
            ))
            .frame(height: 1)
            .frame(maxWidth: 320)
    }
}
