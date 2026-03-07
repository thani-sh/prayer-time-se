import { expect, test } from 'vitest';
import { getPrayerTimes } from './index';

/**
 * Tests for the getPrayerTimes function
 */
test('getPrayerTimes', async () => {
  const result = await getPrayerTimes(new Date('2025-01-30'), 'islamiskaforbundet', 'uppsala');
  expect(result).toEqual({
    fajr: { hour: 5, minute: 41 },
    sunrise: { hour: 8, minute: 1 },
    dhuhr: { hour: 12, minute: 8 },
    asr: { hour: 13, minute: 38 },
    maghrib: { hour: 16, minute: 4 },
    isha: { hour: 18, minute: 9 },
  });
});
