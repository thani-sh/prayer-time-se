//
//  PrayerTimesWidgetProvider.swift
//  PrayerTimesWidget
//
//  Created by Jules on 2025-02-04.
//

import WidgetKit
import SwiftUI

struct PrayerTimesWidgetProvider: TimelineProvider {
    func placeholder(in context: Context) -> PrayerTimesWidgetEntry {
        // Return a dummy entry
        let dummy = PrayerTime(method: .islamiskaforbundet, city: .stockholm, type: .asr, time: Date())
        return PrayerTimesWidgetEntry(date: Date(), prayerTime: dummy)
    }

    func getSnapshot(in context: Context, completion: @escaping (PrayerTimesWidgetEntry) -> Void) {
        // Return current state or dummy
         let nextPrayer = PrayerTime.getNextPrayers(method: .current, city: .current, count: 1).first
         let entry = PrayerTimesWidgetEntry(date: Date(), prayerTime: nextPrayer)
         completion(entry)
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<PrayerTimesWidgetEntry>) -> Void) {
        var entries: [PrayerTimesWidgetEntry] = []
        let currentDate = Date()

        // Get next 5 prayers to populate timeline
        let nextPrayers = PrayerTime.getNextPrayers(method: .current, city: .current, count: 5, date: currentDate)

        if let first = nextPrayers.first {
            // First entry is NOW, showing the first upcoming prayer
            entries.append(PrayerTimesWidgetEntry(date: currentDate, prayerTime: first))

            // Subsequent entries:
            // At the time of prayer P[i], we should start showing P[i+1].
            for i in 0..<(nextPrayers.count - 1) {
                let prayerTime = nextPrayers[i].time
                let nextPrayerContent = nextPrayers[i+1]

                if prayerTime > currentDate {
                     entries.append(PrayerTimesWidgetEntry(date: prayerTime, prayerTime: nextPrayerContent))
                }
            }
        }

        let timeline = Timeline(entries: entries, policy: .atEnd)
        completion(timeline)
    }
}
