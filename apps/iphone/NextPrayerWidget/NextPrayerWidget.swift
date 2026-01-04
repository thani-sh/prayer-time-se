//
//  NextPrayerWidget.swift
//  Bönetider
//
//  Created by Jules on 2025-01-31.
//

import WidgetKit
import SwiftUI

struct Provider: TimelineProvider {
    func placeholder(in context: Context) -> NextPrayerEntry {
        NextPrayerEntry(date: Date(), prayer: samplePrayer)
    }

    func getSnapshot(in context: Context, completion: @escaping (NextPrayerEntry) -> ()) {
        let entry = NextPrayerEntry(date: Date(), prayer: getNextPrayer())
        completion(entry)
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<NextPrayerEntry>) -> ()) {
        var entries: [NextPrayerEntry] = []
        let currentDate = Date()

        // Fetch settings (Note: Shared UserDefaults required for actual user settings sync)
        let method = PrayerTimeMethod.current
        let city = PrayerTimeCity.current

        // Get next prayers to schedule updates
        // We fetch enough to cover a reasonable timeframe (e.g., next 24 hours)
        let prayers = PrayerTime.getNextPrayers(method: method, city: city, count: 10, date: currentDate)

        if let first = prayers.first {
            // The current "next" prayer, valid from now
            entries.append(NextPrayerEntry(date: currentDate, prayer: first))
        }

        // Schedule future updates
        // When prayer[i] time arrives, the next prayer becomes prayer[i+1]
        for i in 0..<(prayers.count - 1) {
            let updateTime = prayers[i].time
            let nextPrayerToDisplay = prayers[i+1]

            // Only add if the update time is in the future relative to now
            if updateTime > currentDate {
                entries.append(NextPrayerEntry(date: updateTime, prayer: nextPrayerToDisplay))
            }
        }

        let timeline = Timeline(entries: entries, policy: .atEnd)
        completion(timeline)
    }

    private func getNextPrayer() -> PrayerTime {
        let method = PrayerTimeMethod.current
        let city = PrayerTimeCity.current
        return PrayerTime.getNextPrayers(method: method, city: city, count: 1).first ?? samplePrayer
    }

    private var samplePrayer: PrayerTime {
        // Fallback/Sample data
        PrayerTime(method: .standard, city: .standard, type: .fajr, time: Date())
    }
}

struct NextPrayerEntry: TimelineEntry {
    let date: Date
    let prayer: PrayerTime
}

struct NextPrayerWidgetEntryView : View {
    var entry: Provider.Entry

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Spacer()

            Text("Next")
                .font(.system(size: 10, weight: .bold))
                .foregroundColor(.secondary)

            Text(entry.prayer.type.label)
                .font(.system(size: 14))
                .foregroundColor(.primary)

            Text(entry.prayer.timeString)
                .font(.system(size: 20, weight: .bold))
                .foregroundColor(.primary)

            Spacer()
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

struct NextPrayerWidget: Widget {
    let kind: String = "NextPrayerWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: Provider()) { entry in
            if #available(iOS 17.0, *) {
                NextPrayerWidgetEntryView(entry: entry)
                    .containerBackground(.background, for: .widget)
            } else {
                NextPrayerWidgetEntryView(entry: entry)
                    .padding()
                    .background(Color(UIColor.systemBackground))
            }
        }
        .configurationDisplayName("Next Prayer")
        .description("Shows the upcoming prayer time.")
        .supportedFamilies([.systemSmall])
    }
}

// Preview
struct NextPrayerWidget_Previews: PreviewProvider {
    static var previews: some View {
        let sample = PrayerTime(method: .standard, city: .stockholm, type: .asr, time: Date())

        if #available(iOS 17.0, *) {
            NextPrayerWidgetEntryView(entry: NextPrayerEntry(date: Date(), prayer: sample))
                .containerBackground(.background, for: .widget)
                .previewContext(WidgetPreviewContext(family: .systemSmall))
        } else {
            NextPrayerWidgetEntryView(entry: NextPrayerEntry(date: Date(), prayer: sample))
                .previewContext(WidgetPreviewContext(family: .systemSmall))
        }
    }
}
