/**
 * List of cities supported by the library.
 */
export const CITIES = [
  'alingsas',
  'amal',
  'angelholm',
  'avesta',
  'bengtsfors',
  'boden',
  'bollnas',
  'boras',
  'borlange',
  'eksjo',
  'enkoping',
  'eskilstuna',
  'eslov',
  'falkenberg',
  'falkoping',
  'filipstad',
  'flen',
  'gallivare',
  'gavle',
  'gislaved',
  'gnosjo',
  'goteborg',
  'halmstad',
  'haparanda',
  'harnosand',
  'hassleholm',
  'helsingborg',
  'hogsby',
  'horby',
  'hudiksvall',
  'hultsfred',
  'jokkmokk',
  'jonkoping',
  'kalmar',
  'kalrshamn',
  'karlskoga',
  'karlskrona',
  'karlstad',
  'katrineholm',
  'kiruna',
  'koping',
  'kristianstad',
  'kristinehamn',
  'laholm',
  'landskrona',
  'lessebo',
  'lidkoping',
  'linkoping',
  'ludvika',
  'lulea',
  'lund',
  'lysekil',
  'malmo',
  'mariestad',
  'marsta',
  'mellerud',
  'mjolby',
  'monsteras',
  'munkedal',
  'nassjo',
  'norrkoping',
  'norrtalje',
  'nybro',
  'nykoping',
  'nynashamn',
  'orebro',
  'ornskoldsvik',
  'oskarshamn',
  'ostersund',
  'oxelosund',
  'pajala',
  'pitea',
  'ronneby',
  'saffle',
  'sala',
  'savsjo',
  'simrishamn',
  'skara',
  'skelleftea',
  'skovde',
  'soderhamn',
  'sodertalje',
  'solleftea',
  'solvesborg',
  'stockholm',
  'strangnas',
  'sundsvall',
  'tierp',
  'tranemo',
  'trelleborg',
  'trollhattan',
  'uddevalla',
  'ulricehamn',
  'umea',
  'uppsala',
  'vanersborg',
  'varberg',
  'varnamo',
  'vasteras',
  'vastervik',
  'vaxjo',
  'vetlanda',
  'vimmerby',
  'visby',
  'ystad',
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
