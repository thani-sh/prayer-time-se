import puppeteer from 'puppeteer';
import { writeFile } from 'node:fs/promises';
import { extractCities, selectCityOption } from './lib/cities.js';
import { extractMonths, selectMonthOption } from './lib/months.js';
import { extractPrayerTimes } from './lib/prayers.js';
import { combinations, waitForAPIRequest } from './lib/shared.js';

// Launch the browser and open a new blank page
const browser = await puppeteer.launch();
const page = await browser.newPage();
await page.setViewport({ width: 1080, height: 1024 });

// Navigate to the islamiskaforbundet prayer times web page
await page.goto('https://www.islamiskaforbundet.se/bonetider/');

// Extract cities and months from the web page and verify them
const [months, cities] = await Promise.all([extractMonths(page), extractCities(page)]);

// Iterate over combinations of cities and months and extract data
for (const city of cities) {
  console.info(`Fetching data for ${city.value}`);
  await selectCityOption(page, city.value);

  const prayerTimesForCity = [];
  for (const month of months) {
    console.info(` -- Fetching data for ${city.value} in ${month.text}`);
    await selectMonthOption(page, month.value);
    await waitForAPIRequest(page, 'Bonetider_Widget.php');
    const prayerTimes = await extractPrayerTimes(page);
    prayerTimesForCity.push(prayerTimes);
  }

  const stringified = JSON.stringify(prayerTimesForCity);
  await Promise.all([
    writeFile(`./src/data/islamiskaforbundet/${city.id}.json`, stringified),
    writeFile(`./src/data/islamiskaforbundet/${city.id}.js`, `export default ${stringified};`),
    writeFile(
      `./src/data/islamiskaforbundet/${city.id}.d.ts`,
      `declare const _default: [number, number, number, number, number, number][][];\nexport default _default;`
    ),
  ]);
}

// Add an imports map
const imports = `
export default {
  islamiskaforbundet: {
    ${cities.map((city) => `${city.id}: () => import('./islamiskaforbundet/${city.id}.js').then((m) => m.default)`).join(',\n    ')}
  } as const
} as const
`;
writeFile(`./src/data/index.ts`, imports);

// Close the browser
await browser.close();
