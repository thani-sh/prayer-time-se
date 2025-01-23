/**
 * Prayer time
 */
export interface Time {
  hour: number;
  minute: number;
}

/**
 * Prayer times for a given day
 */
export interface PrayerTimes {
  fajr: Time;
  sunrise: Time;
  dhuhr: Time;
  asr: Time;
  // asr_hanafi: Time; // --- Not supported yet
  maghrib: Time;
  isha: Time;
}

/**
 * Reutrns prayer times for a given month and day
 */
export declare function forMonthAndDay(
  city: string,
  month: number,
  day: number
): PrayerTimes;

/**
 * Reutrns prayer times for a given date
 */
export declare function forDate(city: string, date: Date): PrayerTimes;

/**
 * Returns prayer times for today
 */
export declare function forToday(city: string): PrayerTimes;

/**
 * Returns a list of cities
 */
export const cities: string[];
