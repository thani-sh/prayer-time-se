import { expect, test } from 'vitest';
import { getPrayerTimes } from './index';

/**
 * Tests for the getPrayerTimes function
 */
test('getPrayerTimes', async () => {
  const result = await getPrayerTimes(new Date('2023-09-17'), 'islamiskaforbundet', 'uppsala');
  expect(result).toEqual({
    asr: { hour: 16, minute: 5 },
    dhuhr: { hour: 12, minute: 49 },
    fajr: { hour: 3, minute: 52 },
    isha: { hour: 21, minute: 16 },
    maghrib: { hour: 19, minute: 13 },
    sunrise: { hour: 6, minute: 15 },
  });
});
