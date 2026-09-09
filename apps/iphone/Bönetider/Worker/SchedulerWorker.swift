//
//  SchedulerWorker.swift
//  Bönetider
//
//  Created by Thanish Nizam on 2025-02-01.
//

import BackgroundTasks
import WidgetKit

// SchedulerWorker runs recurrently and schedules notifications for
// the next N prayers with selected city and methodology.
struct SchedulerWorker {
  // Time interval (in seconds) between worker executions.
  // Note: this can vary and it is decided by the system.
  static let interval: TimeInterval = 60 * 60
  
  // A unique identifier for the scheduler worker bg tasks.
  static let identifier: String = "me.thanish.prayers.se.SchedulerWorker"
  
  // The number of upcoming prayers to schedule in bg task.
  static let toSchedule = 5
  
  // MARK: - Methods
  
  // Sets up the worker when the app is launched by the user.
  static func initialize() {
    SchedulerWorker.scheduleNotifications()
  }

  // Schedule the next execution after the configured interval.
  static func scheduleNextTask() async {
    let pending = await BGTaskScheduler.shared.pendingTaskRequests()
    if pending.count > 0 {
      print(">> SchedulerWorker: pending tasks found, not scheduling")
      return
    }
    let date = Date().addingTimeInterval(interval)
    let task = BGAppRefreshTaskRequest(identifier: identifier)
    task.earliestBeginDate = date
    print(">> SchedulerWorker: scheduling at: \(String(describing: task.earliestBeginDate))")
    try? BGTaskScheduler.shared.submit(task)
  }
  
  // The main function of the worker.
  static func scheduleNotifications() {
    print(">> SchedulerWorker: started")
    Task { await SchedulerWorker.scheduleNextTask() }
    scheduleUpcomingPrayers()
  }

  // Entry point for the background refresh task. It runs inside the task's
  // execution window, so the next refresh is re-armed before the system suspends
  // the app — a detached re-arm can be frozen mid-flight and the chain then dies.
  static func runBackgroundRefresh() async {
    print(">> SchedulerWorker: background refresh started")
    await SchedulerWorker.scheduleNextTask()
    scheduleUpcomingPrayers()
  }

  // Schedules the notifications for the next prayers. This is the half shared by
  // the foreground and background paths, so stale requests must be purged here
  // for both to inherit it.
  static func scheduleUpcomingPrayers() {
    WidgetCenter.shared.reloadAllTimelines()
    PrayerTime
      .getNextPrayers(method: PrayerTimeMethod.current, city: PrayerTimeCity.current, count: toSchedule)
      .forEach { prayer in
      NotificationWorker.schedule(prayer: prayer)
    }
  }
}
