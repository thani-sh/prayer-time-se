//
//  PrayerTimesWidgetView.swift
//  PrayerTimesWidget
//
//  Created by Jules on 2025-02-04.
//

import SwiftUI
import WidgetKit

struct PrayerTimesWidgetView: View {
    var entry: PrayerTimesWidgetProvider.Entry

    var body: some View {
        VStack(alignment: .leading, spacing: 2) {
            if let prayer = entry.prayerTime {
                Spacer()
                
                Text(prayer.type.label.uppercased())
                    .font(.system(size: 12, weight: .bold))
                    .foregroundStyle(.tint)

                Text(prayer.timeString)
                    .font(.system(size: 32, weight: .bold, design: .rounded))
                    .foregroundStyle(.primary)
                    .minimumScaleFactor(0.6)
                    .lineLimit(1)

                Text(prayer.time, style: .relative)
                    .font(.system(size: 10))
                    .foregroundStyle(.secondary)
                    .lineLimit(1)
                
                Spacer()
            } else {
                Text(String(localized: "widget_no_data_label"))
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}
