//
//  PrayerTimesDayView.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-01.
//

import SwiftUI

// PrayerTimesDayView is a view for the table with prayer times for a date.
struct PrayerTimesDayView: View {
  // MARK: - Properties
  
  let method: PrayerTimeMethod
  let city: PrayerTimeCity
  let date: Date
  
  // MARK: - Embedded Structs
  
  // PrayerTimeRow is a view for a row of the table with prayer times.
  // The current prayer time and the next prayer times is emphasized.
  private struct PrayerTimeRow: View {
    let prayerTime: PrayerTime
    
    var body: some View {
      HStack (spacing: 24) {
        Text(prayerTime.type.label)
          .font(.body.weight(.medium))
          .frame(maxWidth: .infinity, alignment: .trailing)
        Text(prayerTime.timeString)
          .font(.body)
          .frame(maxWidth: .infinity, alignment: .leading)
      }
      .foregroundColor(textColor)
    }
    
    private var textColor: Color {
      if prayerTime.isCurrent {
        return .accentColor
      } else if prayerTime.isNext {
        return .primary
      } else {
        return .secondary
      }
    }
  }
  
  // MARK: - View
  
  var body: some View {
    VStack(spacing: 0) {
      let prayerTable = PrayerTimeTable.forDate(method: method, city: city, date: date)
      
      ForEach(prayerTable.data.indices, id: \.self) { index in
        let prayerTime = prayerTable.data[index]

        VStack(spacing: 0) {
          if index != 0 {
            FadingHorizontalDivider().padding(.vertical, 8)
          }
          PrayerTimeRow(prayerTime: prayerTime).padding(.vertical, 12)
        }
      }
    }
    .padding(.horizontal)
  }
}
