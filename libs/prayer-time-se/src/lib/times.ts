import { City, DEFAULT_CITY, isValidCity } from './cities.js';
import { DEFAULT_METHOD, isValidMethod, Method } from './method.js';

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
 * Format a Date object as YYYY-MM-DD string.
 */
function formatDate(date: Date): string {
  const yyyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `${yyyyy}-${mm}-${dd}`;
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

  const formattedDate = formatDate(date);
  const url = `https://api.xn--bnetider-n4a.nu/v1/method/${method}/city/${city}/times/${formattedDate}`;

  const response = await fetch(url);
  if (!response.ok) {
    throw new Error(`Failed to fetch prayer times: ${response.statusText}`);
  }

  const values: number[] = await response.json();
  if (!Array.isArray(values) || values.length !== 6) {
    throw new Error('Invalid response format from API');
  }

  return {
    fajr: minutesToTime(values[0]),
    sunrise: minutesToTime(values[1]),
    dhuhr: minutesToTime(values[2]),
    asr: minutesToTime(values[3]),
    maghrib: minutesToTime(values[4]),
    isha: minutesToTime(values[5]),
  };
}
