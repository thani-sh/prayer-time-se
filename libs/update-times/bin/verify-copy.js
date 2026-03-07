import { readdir, readFile } from "node:fs/promises";
import { createHash } from "node:crypto";
import { dirname, resolve } from "node:path";
import { fileURLToPath } from "node:url";
import { CITIES } from "../src/cities.js";

// Get the directory name of the current module
const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);
const __rootdir = resolve(__dirname, "../../../");

// Method name used in file naming
const method = "islamiskaforbundet";

// Define source and destination directories
const sourceDir = resolve(__rootdir, "data");
const destinations = [
  {
    type: "data",
    dir: "apps/android/app/src/main/assets/values",
    label: "Android Assets",
  },
  {
    type: "data",
    dir: "apps/iphone/Bönetider/Resources/Values",
    label: "iOS Resources",
  },
];

let hasErrors = false;

/**
 * Calculate SHA-256 hash of file content
 */
function hashContent(content) {
  return createHash("sha256").update(content).digest("hex");
}

/**
 * Read and hash all source files
 */
async function getSourceHashes() {
  const hashes = new Map();

  for (const city of CITIES) {
    const fileName = `${method}.${city.id}.json`;
    const filePath = resolve(sourceDir, fileName);

    try {
      const content = await readFile(filePath, "utf-8");
      hashes.set(fileName, {
        hash: hashContent(content),
        content,
      });
    } catch (err) {
      console.error(`❌ Error reading source file ${fileName}: ${err.message}`);
      hasErrors = true;
    }
  }

  return hashes;
}

/**
 * Verify a data destination (JSON files)
 */
async function verifyDataDestination(destination, sourceHashes) {
  const destPath = resolve(__rootdir, destination.dir);
  console.log(`\n📂 Checking ${destination.label}: ${destination.dir}`);

  let missingFiles = [];
  let mismatchedFiles = [];
  let matchedFiles = 0;

  try {
    const files = await readdir(destPath);

    for (const city of CITIES) {
      const fileName = `${method}.${city.id}.json`;

      if (!files.includes(fileName)) {
        missingFiles.push({ city: city.name, file: fileName });
        continue;
      }

      const filePath = resolve(destPath, fileName);
      const content = await readFile(filePath, "utf-8");
      const destHash = hashContent(content);
      const sourceHash = sourceHashes.get(fileName)?.hash;

      if (destHash !== sourceHash) {
        mismatchedFiles.push({ city: city.name, file: fileName });
      } else {
        matchedFiles++;
      }
    }
  } catch (err) {
    console.error(`   ❌ Error accessing destination: ${err.message}`);
    return false;
  }

  if (missingFiles.length > 0) {
    console.error(`   ❌ Missing ${missingFiles.length} file(s):`);
    missingFiles.forEach(({ city, file }) => {
      console.error(`      - ${city}: ${file}`);
    });
    hasErrors = true;
  }

  if (mismatchedFiles.length > 0) {
    console.error(`   ❌ Mismatched ${mismatchedFiles.length} file(s):`);
    mismatchedFiles.forEach(({ city, file }) => {
      console.error(`      - ${city}: ${file}`);
    });
    hasErrors = true;
  }

  if (missingFiles.length === 0 && mismatchedFiles.length === 0) {
    console.log(`   ✅ All ${matchedFiles} files match source`);
    return true;
  }

  return false;
}

/**
 * Main verification function
 */
async function main() {
  console.log("🔄 Verifying prayer times data copied to all destinations...\n");

  console.log("📋 Reading source files...");
  const sourceHashes = await getSourceHashes();
  console.log(`✅ Read ${sourceHashes.size} source files`);

  for (const destination of destinations) {
    await verifyDataDestination(destination, sourceHashes);
  }

  console.log("\n" + "=".repeat(50));
  if (hasErrors) {
    console.error("❌ Verification FAILED - destinations are out of sync");
    console.error("   Run 'yarn copy' to synchronize destinations");
    process.exit(1);
  } else {
    console.log("✅ Verification PASSED - all destinations in sync");
    process.exit(0);
  }
}

main();
