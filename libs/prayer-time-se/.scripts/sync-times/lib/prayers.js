import * as cheerio from 'cheerio';

/**
 * URL of the API endpoint that provides the prayer times.
 */
const API_URL =
  'https://www.islamiskaforbundet.se/wp-content/plugins/bonetider/Bonetider_Widget.php';

/**
 * Headers to send with the request to the API.
 */
const HEADERS = {
  'content-type': 'application/x-www-form-urlencoded; charset=UTF-8',
};

/**
 * Converts a time string to the number of minutes since midnight.
 */
function toMinuteOfDay(timeString) {
  const [hours, minutes] = timeString.split(':').map(Number);
  const totalMinutes = hours * 60 + minutes;
  if (totalMinutes < 0 || totalMinutes > 24 * 60 || Number.isNaN(totalMinutes)) {
    throw new Error(`Invalid time string: ${timeString}`);
  }
  return totalMinutes;
}

/**
 * Fetches the prayer times for a specific city.
 */
export async function fetchPrayerTimesForCity(city) {
  const promises = [];
  for (let month = 1; month <= 12; month++) {
    promises.push(fetchPrayerTime(city.name, month));
  }
  return Promise.all(promises);
}

/**
 * Fetches the prayer times for a specific city and month.
 */
export async function fetchPrayerTime(city, month) {
  const params = [`ifis_bonetider_page_city=${city}%2C+SE`, `ifis_bonetider_page_month=${month}`];
  const res = await fetch(API_URL, { method: 'POST', headers: HEADERS, body: params.join('&') });
  if (!res.ok) {
    throw new Error(`Failed to fetch prayer times for ${city} in month ${month}`);
  }
  const html = await res.text();
  return extractPrayerTimes(html);
}

/**
 * Extracts the prayer times from the web page.
 */
export async function extractPrayerTimes(html) {
  const $ = cheerio.load(html);
  const $tr = $('table tr');

  const prayerTimes = [];

  $tr.each((i, row) => {
    if (i === 0) {
      return;
    }

    const cellValues = [];
    $(row)
      .find('td')
      .each((j, cell) => cellValues.push($(cell).text().trim()));

    if (cellValues.length !== 7) {
      throw new Error(
        `Unexpected number of cells in row ${i + 1}: ${cellValues.length}: ${$(row).html()}`
      );
    }

    const [date, fajr, sunrise, dhuhr, asr, maghrib, isha] = cellValues;

    if (i !== Number(date)) {
      throw new Error(`Unexpected date in row ${i + 1}: ${date}`);
    }

    prayerTimes[i - 1] = [
      toMinuteOfDay(fajr),
      toMinuteOfDay(sunrise),
      toMinuteOfDay(dhuhr),
      toMinuteOfDay(asr),
      toMinuteOfDay(maghrib),
      toMinuteOfDay(isha),
    ];
  });

  return prayerTimes;
}
