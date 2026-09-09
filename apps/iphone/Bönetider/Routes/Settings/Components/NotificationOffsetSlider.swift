//
//  NotificationOffsetSlider.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-01.
//

import SwiftUI

// NotificationOffsetSlider is a SwiftUI View for selecting the delay
struct NotificationOffsetSlider: View {
  @Binding
  var offset: NotificationOffset
  
  var body: some View {
    List {
      Text(String(localized: "notification_before_adhan_offset"))
        .badge(offset.label)
      if offset.enabled {
        Slider(
          value: Binding(
            get: { Double(exactly: offset.minutes)! },
            set: { offset = .init(Int($0)) }
          ),
          in: Double(NotificationOffset.min + 1)...Double(NotificationOffset.max),
          step: 1
        )
        .onChange(of: offset) { _, _ in SchedulerWorker.scheduleNotifications() }
      }
    }
  }
}
