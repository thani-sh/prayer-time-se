import { readdir, readFile, stat } from "node:fs/promises";
import { dirname, resolve } from "node:path";
import { fileURLToPath } from "node:url";
import { CITIES } from "../src/cities.js";

// Get the directory name of the current module
const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);
const __rootdir = resolve(__dirname, "../../../");

// Define the data directory path relative to the current module
const dataDir = resolve(__rootdir, "data");

// Method name used in file naming
const method = "islamiskaforbundet";

// Maximum age in days before data is considered stale
const MAX_AGE_DAYS = 7;

let hasErrors = false;

/**
 * Check if data directory exists and is accessible
 */
async function checkDataDirectory() {
  try {
    const stats = await stat(dataDir);
    if (!stats.isDirectory()) {
      console.error(`❌ Error: ${dataDir} is not a directory`);
      return false;
    }
    console.log(`✅ Data directory exists: ${dataDir}`);
    return true;
  } catch (err) {
    console.error(`❌ Error: Data directory not found at ${dataDir}`);
    return false;
  }
}

/**
 * Verify all expected city files exist
 */
async function checkAllCitiesExist() {
  console.log(`\n📋 Checking for all ${CITIES.length} city files...`);

  const files = await readdir(dataDir);
  const missingCities = [];

  for (const city of CITIES) {
    const expectedFile = `${method}.${city.id}.json`;
    if (!files.includes(expectedFile)) {
      missingCities.push({ city: city.name, file: expectedFile });
    }
  }

  if (missingCities.length > 0) {
    console.error(`❌ Missing ${missingCities.length} city file(s):`);
    missingCities.forEach(({ city, file }) => {
      console.error(`   - ${city}: ${file}`);
    });
    return false;
  }

  console.log(`✅ All ${CITIES.length} city files present`);
  return true;
}

/**
 * Validate JSON structure and content of data files
 */
async function validateDataFiles() {
  console.log(`\n🔍 Validating JSON structure and content...`);

  let validFiles = 0;
  let invalidFiles = [];

  for (const city of CITIES) {
    const fileName = `${method}.${city.id}.json`;
    const filePath = resolve(dataDir, fileName);

    try {
      const content = await readFile(filePath, "utf-8");
      const data = JSON.parse(content);

      // Validate structure: should be array of 12 months
      if (!Array.isArray(data)) {
        invalidFiles.push({ city: city.name, reason: "Not an array" });
        continue;
      }

      if (data.length !== 12) {
        invalidFiles.push({ city: city.name, reason: `Expected 12 months, got ${data.length}` });
        continue;
      }

      // Validate each month contains prayer time arrays
      for (let month = 0; month < 12; month++) {
        if (!Array.isArray(data[month])) {
          invalidFiles.push({ city: city.name, reason: `Month ${month + 1} is not an array` });
          continue;
        }

        // Each day should have 6 prayer times
        for (let day = 0; day < data[month].length; day++) {
          if (!Array.isArray(data[month][day]) || data[month][day].length !== 6) {
            invalidFiles.push({
              city: city.name,
              reason: `Month ${month + 1}, Day ${day + 1} has invalid prayer times`
            });
            break;
          }
        }
      }

      validFiles++;
    } catch (err) {
      invalidFiles.push({ city: city.name, reason: err.message });
    }
  }

  if (invalidFiles.length > 0) {
    console.error(`❌ Found ${invalidFiles.length} invalid file(s):`);
    invalidFiles.forEach(({ city, reason }) => {
      console.error(`   - ${city}: ${reason}`);
    });
    return false;
  }

  console.log(`✅ All ${validFiles} files have valid JSON structure`);
  return true;
}

/**
 * Check modification time of data files
 */
async function checkDataFreshness() {
  console.log(`\n⏰ Checking data freshness (max age: ${MAX_AGE_DAYS} days)...`);

  const now = Date.now();
  const maxAgeMs = MAX_AGE_DAYS * 24 * 60 * 60 * 1000;
  const staleFiles = [];

  for (const city of CITIES) {
    const fileName = `${method}.${city.id}.json`;
    const filePath = resolve(dataDir, fileName);

    try {
      const stats = await stat(filePath);
      const age = now - stats.mtimeMs;
      const ageDays = Math.floor(age / (24 * 60 * 60 * 1000));

      if (age > maxAgeMs) {
        staleFiles.push({ city: city.name, ageDays });
      }
    } catch (err) {
      // File doesn't exist, already caught in previous check
    }
  }

  if (staleFiles.length > 0) {
    console.warn(`⚠️  Found ${staleFiles.length} stale file(s):`);
    staleFiles.forEach(({ city, ageDays }) => {
      console.warn(`   - ${city}: ${ageDays} days old`);
    });
    return false;
  }

  console.log(`✅ All files are fresh (< ${MAX_AGE_DAYS} days old)`);
  return true;
}

/**
 * Main verification function
 */
async function main() {
  console.log("🔄 Verifying synced prayer times data...\n");

  const dirExists = await checkDataDirectory();
  if (!dirExists) {
    hasErrors = true;
  } else {
    const allExist = await checkAllCitiesExist();
    const allValid = await validateDataFiles();
    const allFresh = await checkDataFreshness();

    if (!allExist || !allValid || !allFresh) {
      hasErrors = true;
    }
  }

  console.log("\n" + "=".repeat(50));
  if (hasErrors) {
    console.error("❌ Verification FAILED - please run sync again");
    process.exit(1);
  } else {
    console.log("✅ Verification PASSED - all data is synced correctly");
    process.exit(0);
  }
}

main();
