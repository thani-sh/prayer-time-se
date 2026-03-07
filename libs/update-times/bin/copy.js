import { mkdir, rm, readdir, readFile, writeFile } from "node:fs/promises";
import { dirname, resolve } from "node:path";
import { fileURLToPath } from "node:url";

// Get the directory name of the current module
const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);
const __rootdir = resolve(__dirname, "../../../");

// Define the data directory path relative to the current module
const sourceDir = resolve(__dirname, __rootdir, "data");

// Destination directories and the expected format for the data
const targets = [
  { dir: "apps/android/app/src/main/assets/values" },
  { dir: "apps/iphone/Bönetider/Resources/Values" },
];

// Clean destination dirs
for (const { dir } of targets) {
  const resolvedDir = resolve(__dirname, __rootdir, dir);
  await rm(resolvedDir, { recursive: true, force: true });
  await mkdir(resolvedDir);
}

// Read all source data in an array of { name, data }
const sourceFileList = await readdir(sourceDir);
const sourceFileData = await Promise.all(
  sourceFileList.map(async (name) => {
    const filePath = resolve(__dirname, __rootdir, sourceDir, name);
    const data = await readFile(filePath, "utf-8");
    return { name, data };
  }),
);

// Write formatted files to all output directories
for (const { dir } of targets) {
  for (const sourceFile of sourceFileData) {
    const filePath = resolve(__dirname, __rootdir, dir, sourceFile.name);
    await writeFile(filePath, sourceFile.data);
  }
}
