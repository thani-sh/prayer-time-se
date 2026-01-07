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

                Text(String(localized: "widget_next_prayer_label"))
                    .font(.system(size: 10, weight: .bold))
                    .foregroundStyle(.tertiary)

                Text(prayer.type.label)
                    .font(.system(size: 16, weight: .medium, design: .default))
                    .foregroundStyle(.secondary)

                Text(prayer.timeString)
                    .font(.system(size: 34, weight: .semibold, design: .rounded))
                    .foregroundStyle(.primary)
                    .minimumScaleFactor(0.6)
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
