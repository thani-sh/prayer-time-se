import assert from "node:assert";
import { describe, it } from "node:test";

import { forDate, forMonthAndDay } from "./index.js";

/**
 * Tests for the forMonthAndDay function
 */
describe("forMonthAndDay", () => {
  it("should return correct prayer times for a given month and day", async () => {
    const result = await forMonthAndDay("uppsala", 9, 17);
    assert.deepEqual(result, {
      asr: { hour: 16, minute: 5 },
      dhuhr: { hour: 12, minute: 49 },
      fajr: { hour: 3, minute: 52 },
      isha: { hour: 21, minute: 16 },
      maghrib: { hour: 19, minute: 13 },
      sunrise: { hour: 6, minute: 15 },
    });
  });
});

/**
 * Tests for the forDate function
 */
describe("forDate", () => {
  it("should return correct prayer times for a given date", async () => {
    const result = await forDate("uppsala", new Date("2023-09-17"));
    assert.deepEqual(result, {
      asr: { hour: 16, minute: 5 },
      dhuhr: { hour: 12, minute: 49 },
      fajr: { hour: 3, minute: 52 },
      isha: { hour: 21, minute: 16 },
      maghrib: { hour: 19, minute: 13 },
      sunrise: { hour: 6, minute: 15 },
    });
  });
});
