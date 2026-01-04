# Widget Setup Instructions

Since this environment does not allow modifying the Xcode project file directly to add a new target, you must manually add the widget target to your project.

1.  **Open Xcode.**
2.  **Add a Widget Extension Target:**
    *   File > New > Target...
    *   Select "Widget Extension".
    *   Name it `NextPrayerWidget`.
    *   Uncheck "Include Configuration App Intent" if present (we use StaticConfiguration).
    *   Finish.
3.  **Replace Files:**
    *   Delete the default `NextPrayerWidget.swift` and `NextPrayerWidgetBundle.swift` created by Xcode.
    *   Add the files from `apps/iphone/NextPrayerWidget/` to the new target.
4.  **Share Domain Code:**
    *   Select the following files in `apps/iphone/Bönetider/Domain/`:
        *   `PrayerTime.swift`
        *   `PrayerTimeCity.swift`
        *   `PrayerTimeData.swift`
        *   `PrayerTimeMethod.swift`
        *   `PrayerTimeTable.swift`
        *   `PrayerTimeType.swift`
        *   `HijriCalendarOffset.swift` (if needed)
        *   `NotificationOffset.swift` (if needed)
    *   In the File Inspector (Right panel), check the box for the `NextPrayerWidget` target under "Target Membership".
5.  **App Groups (Crucial for Settings Sync):**
    *   The widget code uses `PrayerTimeMethod.current` and `PrayerTimeCity.current`. These currently use `UserDefaults.standard`.
    *   To share the selected City and Method between the App and the Widget, you MUST enable **App Groups**.
    *   **In Xcode:**
        *   Select the Project > `Bönetider` Target > Signing & Capabilities.
        *   + Capability > App Groups.
        *   Add a group (e.g., `group.com.yourcompany.bonetider`).
        *   Repeat for the `NextPrayerWidget` Target.
    *   **Update Code:**
        *   Modify `PrayerTimeMethod.swift` and `PrayerTimeCity.swift` to use `UserDefaults(suiteName: "group.com.yourcompany.bonetider")` instead of `standard`.
