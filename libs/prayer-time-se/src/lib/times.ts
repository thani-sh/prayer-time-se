import { City, DEFAULT_CITY, isValidCity } from './cities';
import { DEFAULT_METHOD, isValidMethod, Method } from './method';

/**
 * The number of minutes in a day.
 */
const MINUTES_PER_DAY = 24 * 60;

/**
 * Type for prayer times for a specific date.
 */
export interface PrayerTimes {
  fajr: { hour: number; minute: number };
  sunrise: { hour: number; minute: number };
  dhuhr: { hour: number; minute: number };
  asr: { hour: number; minute: number };
  maghrib: { hour: number; minute: number };
  isha: { hour: number; minute: number };
}

/**
 * Convert minutes to TimeOfDay. Throws an error if the value is not valid.
 */
function minutesToTime(minutes: number) {
  if (minutes < 0 || minutes >= MINUTES_PER_DAY) {
    throw new Error(`Invalid minutes value: ${minutes}`);
  }
  return {
    hour: Math.floor(minutes / 60),
    minute: minutes % 60,
  };
}

/**
 * Get prayer times for the given city, method and date.
 */
export async function getPrayerTimes(
  date: Date = new Date(),
  method: Method = DEFAULT_METHOD,
  city: City = DEFAULT_CITY
): Promise<PrayerTimes> {
  if (!isValidMethod(method)) {
    throw new Error(`Invalid method: ${method}`);
  }
  if (!isValidCity(city)) {
    throw new Error(`Invalid city: ${city}`);
  }
  const d = date.getDate();
  const m = date.getMonth();
  const values = await import(`../data/${method}.${city}.js`).then((module) => module.default);
  return {
    fajr: minutesToTime(values[m][d - 1][0]),
    sunrise: minutesToTime(values[m][d - 1][1]),
    dhuhr: minutesToTime(values[m][d - 1][2]),
    asr: minutesToTime(values[m][d - 1][3]),
    maghrib: minutesToTime(values[m][d - 1][4]),
    isha: minutesToTime(values[m][d - 1][5]),
  };
}
