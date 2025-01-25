import { toIdentifier } from './shared.js';

/**
 * List of city names expected to be under the select element.
 */
const EXPECTED_VALUES = [
  'Alingsås',
  'Avesta',
  'Bengtsfors',
  'Boden',
  'Bollnäs',
  'Borlänge',
  'Borås',
  'Eksjö',
  'Enköping',
  'Eskilstuna',
  'Eslöv',
  'Falkenberg',
  'Falköping',
  'Filipstad',
  'Flen',
  'Gislaved',
  'Gnosjö',
  'Gällivare',
  'Gävle',
  'Göteborg',
  'Halmstad',
  'Haparanda',
  'Helsingborg',
  'Hudiksvall',
  'Hultsfred',
  'Härnösand',
  'Hässleholm',
  'Högsby',
  'Hörby',
  'Jokkmokk',
  'Jönköping',
  'Kalmar',
  'Kalrshamn',
  'Karlskoga',
  'Karlskrona',
  'Karlstad',
  'Katrineholm',
  'Kiruna',
  'Kristianstad',
  'Kristinehamn',
  'Köping',
  'Laholm',
  'Landskrona',
  'Lessebo',
  'Lidköping',
  'Linköping',
  'Ludvika',
  'Luleå',
  'Lund',
  'Lysekil',
  'Malmö',
  'Mariestad',
  'Mellerud',
  'Mjölby',
  'Munkedal',
  'Märsta',
  'Mönsterås',
  'Norrköping',
  'Norrtälje',
  'Nybro',
  'Nyköping',
  'Nynäshamn',
  'Nässjö',
  'Oskarshamn',
  'Oxelösund',
  'Pajala',
  'Piteå',
  'Ronneby',
  'Sala',
  'Simrishamn',
  'Skara',
  'Skellefteå',
  'Skövde',
  'Sollefteå',
  'Stockholm',
  'Strängnäs',
  'Sundsvall',
  'Säffle',
  'Sävsjö',
  'Söderhamn',
  'Södertälje',
  'Sölvesborg',
  'Tierp',
  'Tranemo',
  'Trelleborg',
  'Trollhättan',
  'Uddevalla',
  'Ulricehamn',
  'Umeå',
  'Uppsala',
  'Varberg',
  'Vetlanda',
  'Vimmerby',
  'Visby',
  'Vänersborg',
  'Värnamo',
  'Västervik',
  'Västerås',
  'Växjö',
  'Ystad',
  'Ängelholm',
  'Åmål',
  'Örebro',
  'Örnsköldsvik',
  'Östersund',
];

/**
 * Locates the select element containing the list of cities.
 */
export async function getCitiesSelect(page) {
  return page.locator('#ifis_bonetider_page_cities').waitHandle();
}

/**
 * Selects a city option from the drop-down list on the page.
 */
export async function selectCityOption(page, value) {
  return page.select('#ifis_bonetider_page_cities', value);
}

/**
 * Extracts the list of cities from the web page and validate them.
 */
export async function extractCities(page) {
  const selectElement = await getCitiesSelect(page);
  const parsedOptions = await selectElement.$$eval('option', (elements) =>
    elements.map((el) => ({ text: el.textContent?.trim(), value: el.value }))
  );
  const values = parsedOptions
    .filter((op) => EXPECTED_VALUES.includes(op.text))
    .map((op) => ({ ...op, id: toIdentifier(op.text) }));
  if (values.length !== EXPECTED_VALUES.length) {
    throw new Error('Some cities are missing on the web page');
  }
  return values;
}
