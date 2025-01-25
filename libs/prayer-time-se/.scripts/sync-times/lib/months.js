import { toIdentifier } from './shared.js';

/**
 * List of month names expected to be under the select element.
 */
const EXPECTED_VALUES = [
  'Januari',
  'Februari',
  'Mars',
  'April',
  'Maj',
  'Juni',
  'Juli',
  'Augusti',
  'September',
  'Oktober',
  'November',
  'December',
];

/**
 * Locates the select element containing the list of months.
 */
export async function getMonthsSelect(page) {
  return page.locator('#ifis_bonetider_page_months').waitHandle();
}

/**
 * Selects a month option from the drop-down list on the page.
 */
export async function selectMonthOption(page, value) {
  return page.select('#ifis_bonetider_page_months', value);
}

/**
 * Extracts the list of months from the web page and validate them.
 */
export async function extractMonths(page) {
  const selectElement = await getMonthsSelect(page);
  const parsedOptions = await selectElement.$$eval('option', (elements) =>
    elements.map((el) => ({ text: el.textContent?.trim(), value: el.value }))
  );
  const values = parsedOptions
    .filter((op) => EXPECTED_VALUES.includes(op.text))
    .map((op) => ({ ...op, idx: EXPECTED_VALUES.indexOf(op.text) }));
  if (values.length !== EXPECTED_VALUES.length) {
    throw new Error('Some months are missing on the web page');
  }
  return values;
}
