//
//  NotificationOffsetSlider.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-01.
//

import SwiftUI

// HijriCalendarOffsetSlider is a SwiftUI View for selecting the hijri offset
struct HijriCalendarOffsetSlider: View {
  @Binding
  var offset: HijriCalendarOffset
  
  var body: some View {
    List {
      Text(String(localized: "hijri_calendar_offset_label"))
        .badge(offset.label)
      Slider(
        value: Binding(
          get: { Double(exactly: offset.days)! },
          set: { offset = .init(Int($0)) }
        ),
        in: Double(HijriCalendarOffset.min)...Double(HijriCalendarOffset.max),
        step: 1
      )
    }
  }
}
