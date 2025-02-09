import { writeFile, rm, mkdir } from 'node:fs/promises';
import { CITIES } from './lib/cities.js';
import { fetchPrayerTimesForCity } from './lib/prayers.js';

// Reset the data directory by deleting and recreating it
await rm('./src/data', { recursive: true, force: true });
await rm('./json', { recursive: true, force: true });
await mkdir('./src/data');
await mkdir('./json');

// Iterate over combinations of cities and months and extract data
for (const city of CITIES) {
  console.info(`Fetching data for ${city.name}`);
  const prayerTimes = await fetchPrayerTimesForCity(city);
  const stringified = JSON.stringify(prayerTimes);
  await Promise.all([
    writeFile(`./json/islamiskaforbundet.${city.id}.json`, stringified),
    writeFile(`./src/data/islamiskaforbundet.${city.id}.js`, `export default ${stringified};`),
    writeFile(
      `./src/data/islamiskaforbundet.${city.id}.d.ts`,
      `declare const _default: [number, number, number, number, number, number][][];\nexport default _default;`
    ),
  ]);
}

// Add an imports map
const imports = `
export default {
  islamiskaforbundet: {
    ${CITIES.map((city) => `${city.id}: () => import('./islamiskaforbundet.${city.id}.js').then((m) => m.default)`).join(',\n    ')}
  } as const
} as const
`;

// Write the imports map to a file
writeFile(`./src/data/index.ts`, imports);
