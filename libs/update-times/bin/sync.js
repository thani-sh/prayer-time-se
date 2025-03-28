import { mkdir, rm, writeFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { CITIES } from "../src/cities.js";
import { fetchPrayerTimesForCity } from "../src/prayers.js";

// Get the directory name of the current module
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const __rootdir = path.resolve(__dirname, "../../../");

// Data is from islamiskaforbundet.se
const method = "islamiskaforbundet";

// Define the data directory path relative to the current module
const outputDir = path.resolve(__dirname, __rootdir, "data");

// Reset the data directory by deleting and recreating it
await rm(outputDir, { recursive: true, force: true });
await mkdir(outputDir);

// Iterate over combinations of cities and months and extract data
for (const city of CITIES) {
  console.info(`Fetching data for ${city.name}`);
  const prayerTimes = await fetchPrayerTimesForCity(city);
  const stringified = JSON.stringify(prayerTimes);
  // Define the file path relative to the data directory
  const filePath = path.join(outputDir, `${method}.${city.id}.json`);
  await writeFile(filePath, stringified);
}
