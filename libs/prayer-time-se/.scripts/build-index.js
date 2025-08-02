#!/usr/bin/env node

import { readdir, writeFile } from 'fs/promises';
import { join, dirname } from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// Paths relative to the script location
const DATA_DIR = join(__dirname, '../src/data');
const CITIES_FILE = join(__dirname, '../src/lib/cities.ts');
const METHODS_FILE = join(__dirname, '../src/lib/method.ts');
const DATA_INDEX_FILE = join(__dirname, '../src/data/index.ts');

async function main() {
  const files = await readdir(DATA_DIR);
  const dataFiles = files.filter((file) => file.endsWith('.ts'));
  const methods = new Set();
  const cities = new Set();
  const dataMap = new Map();
  for (const file of dataFiles) {
    const parts = file.replace('.ts', '').split('.');
    if (parts.length >= 2) {
      const method = parts[0];
      const city = parts.slice(1).join('.');
      methods.add(method);
      cities.add(city);
      if (!dataMap.has(method)) {
        dataMap.set(method, new Map());
      }
      dataMap.get(method).set(city, file.replace('.ts', ''));
    }
  }
  const sortedMethods = Array.from(methods).sort();
  const sortedCities = Array.from(cities).sort();
  const citiesContent = generateCitiesFile(sortedCities);
  await writeFile(CITIES_FILE, citiesContent, 'utf8');
  const methodsContent = generateMethodsFile(sortedMethods);
  await writeFile(METHODS_FILE, methodsContent, 'utf8');
  const dataIndexContent = generateDataIndexFile(dataMap);
  await writeFile(DATA_INDEX_FILE, dataIndexContent, 'utf8');
}

function generateCitiesFile(cities) {
  const citiesArray = cities.map((city) => `  '${city}'`).join(',\n');
  return `/**
 * List of cities supported by the library.
 */
export const CITIES = [
${citiesArray},
] as const;

/**
 * Default city to use if none is provided.
 */
export const DEFAULT_CITY: City = 'stockholm';

/**
 * Type for cities supported by the library.
 */
export type City = (typeof CITIES)[number];

/**
 * Check if the given value is a valid city.
 */
export function isValidCity(city: any): city is City {
  return CITIES.includes(city);
}
`;
}

function generateMethodsFile(methods) {
  const methodsArray = methods.map((method) => `'${method}'`).join(', ');
  return `/**
 * List of methods supported by the library.
 */
export const METHODS = [${methodsArray}] as const;

/**
 * Default method to use if none is provided.
 */
export const DEFAULT_METHOD: Method = '${methods[0]}';

/**
 * Type for methods supported by the library.
 */
export type Method = (typeof METHODS)[number];

/**
 * Check if the given value is a valid method.
 */
export function isValidMethod(method: any): method is Method {
  return METHODS.includes(method);
}
`;
}

function generateDataIndexFile(dataMap) {
  const methods = Array.from(dataMap.keys()).sort();
  const methodEntries = methods
    .map((method) => {
      const cities = Array.from(dataMap.get(method).keys()).sort();
      const cityEntries = cities
        .map((city) => {
          const fileName = dataMap.get(method).get(city);
          return `    ${city}: () => import("./${fileName}.js").then(m => m.default)`;
        })
        .join(',\n');
      return `  ${method}: {\n${cityEntries},\n  }`;
    })
    .join(',\n');
  return `export default {
${methodEntries},
};
`;
}

// Run the script
main();
