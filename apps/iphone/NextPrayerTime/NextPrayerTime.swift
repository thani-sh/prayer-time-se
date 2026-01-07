//
//  NextPrayerTime.swift
//  NextPrayerTime
//
//  Created by Thanish Nizam on 2026-01-07.
//

import WidgetKit
import SwiftUI

struct NextPrayerTime: Widget {
    let kind: String = "NextPrayerTime"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: PrayerTimesWidgetProvider()) { entry in
            if #available(iOS 17.0, *) {
                PrayerTimesWidgetView(entry: entry)
                    .containerBackground(.fill.tertiary, for: .widget)
            } else {
                PrayerTimesWidgetView(entry: entry)
                    .padding()
                    .background()
            }
        }
        .configurationDisplayName("Next Prayer")
        .description("Shows the next prayer time.")
        .supportedFamilies([.systemSmall])
    }
}
