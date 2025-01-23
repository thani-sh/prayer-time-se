import cities from "./cities.js";

/**
 * Returns a Time object with hour and minute.
 * @param {number} hour
 * @param {number} minute
 * @returns {Time}
 */
function createTime(minutes) {
  const hour = Math.floor(minutes / 60);
  const minute = minutes % 60;
  return { hour, minute };
}

/**
 * Reutrns prayer times for a given month and day
 * @param {number} month
 * @param {number} day
 * @returns {PrayerTimes}
 */
export async function forMonthAndDay(city, month, day) {
  if (cities.indexOf(city) === -1) {
    throw new Error(`City "${city}" is not supported`);
  }
  const times = await import(`./values/${city}.js`).then((m) => m.default);
  const data = times[month - 1][day - 1];
  return {
    fajr: createTime(data[0]),
    sunrise: createTime(data[1]),
    dhuhr: createTime(data[2]),
    asr: createTime(data[3]),
    // asr_hanafi: createTime(data[4]), // --- Not supported yet
    maghrib: createTime(data[5]),
    isha: createTime(data[6]),
  };
}

/**
 * Reutrns prayer times for a given date
 * @param {Date} date
 * @returns {PrayerTimes}
 */
export async function forDate(city, date) {
  const m = date.getMonth() + 1;
  const d = date.getDate();
  return await forMonthAndDay(city, m, d);
}

/**
 * Returns prayer times for today
 * @returns {PrayerTimes}
 */
export async function forToday(city) {
  return await forDate(city, new Date());
}

/**
 * Export a list of supported cities
 */
export { default as cities } from "./cities.js";
