/**
 * Locates the table body element containing the prayer times.
 */
export async function getTableBodyElement(page) {
  return page.locator('#ifis_bonetider').waitHandle();
}

/**
 * Converts a time string to the number of minutes since midnight.
 */
function toMinuteOfDay(timeString) {
  const [hours, minutes] = timeString.split(':').map(Number);
  return hours * 60 + minutes;
}

/**
 * Extracts the prayer times from the web page.
 */
export async function extractPrayerTimes(page) {
  const tableElement = await getTableBodyElement(page);
  const parsedTimetable = await tableElement.$$eval('tr', (rows) =>
    rows.map((row) => ({
      date: row.children[0].textContent?.trim(),
      fajr: row.children[1].textContent?.trim(),
      sunrise: row.children[2].textContent?.trim(),
      dhuhr: row.children[3].textContent?.trim(),
      asr: row.children[4].textContent?.trim(),
      maghrib: row.children[5].textContent?.trim(),
      isha: row.children[6].textContent?.trim(),
    }))
  );
  return parsedTimetable.map((row) => [
    toMinuteOfDay(row.fajr),
    toMinuteOfDay(row.sunrise),
    toMinuteOfDay(row.dhuhr),
    toMinuteOfDay(row.asr),
    toMinuteOfDay(row.maghrib),
    toMinuteOfDay(row.isha),
  ]);
}
