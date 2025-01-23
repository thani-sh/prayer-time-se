import { writeFileSync } from "node:fs";
import cities from "../cities.js";

/**
 * Get prayer times for a city.
 */
async function getTimesForCity(city) {
  const timesForCity = [];
  for (let i = 1; i <= 12; ++i) {
    const timesForMonth = await getTimesForCityAndMonth(city, i);
    timesForCity.push(timesForMonth);
  }
  return timesForCity;
}

/**
 * Get prayer times for a city and month.
 */
async function getTimesForCityAndMonth(city, month) {
  // Example CURL command to sync a single file:
  //     curl 'https://www.islamiskaforbundet.se/wp-content/plugins/bonetider/Bonetider_Widget.php' \
  //       -H 'cache-control: no-cache' \
  //       -H 'content-type: application/x-www-form-urlencoded; charset=UTF-8' \
  //       -H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36' \
  //       --data-raw 'ifis_bonetider_page_city=Stockholm%2C+SE&ifis_bonetider_page_month=12'
  const requestUrl =
    "https://www.islamiskaforbundet.se/wp-content/plugins/bonetider/Bonetider_Widget.php";
  const requestBody = [
    `ifis_bonetider_page_city=${encodeURIComponent(city)}, SE`,
    `ifis_bonetider_page_month=${month + 13}`,
  ].join("&");
  const userAgent =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36";
  const requestHeaders = {
    "cache-control": "no-cache",
    "content-type": "application/x-www-form-urlencoded; charset=UTF-8",
    "user-agent": userAgent,
  };
  const res = await fetch(requestUrl, {
    method: "POST",
    body: requestBody,
    headers: requestHeaders,
  });
  if (!res.ok) {
    throw new Error(
      `Request failed for fetching prayer times with status ${res.status}`
    );
  }
  const html = await res.text();
  const cells = html
    .match(/<td.*?>(.*?)<\/td>/g)
    .map((cell) => cell.replace(/<.*?>/g, "").trim());
  const timesForMonth = [];
  for (let i = 0; i < cells.length; i += 7) {
    const selectedDay = parseInt(cells[i], 10);
    const timesForDay = [];
    for (let j = 0; j < 6; j++) {
      const [h, m] = cells[i + j + 1].split(":").map(Number);
      timesForDay.push(h * 60 + m);
    }
    timesForMonth[selectedDay - 1] = timesForDay;
  }
  return timesForMonth;
}

/**
 * MAIN
 */
for (const city of cities) {
  console.info(`Syncing prayer times for ${city}`);
  const timesForCity = await getTimesForCity(city);
  const stringified = JSON.stringify(timesForCity);
  writeFileSync(`./values/${city}.json`, stringified);
  writeFileSync(`./values/${city}.js`, "export default " + stringified);
}
