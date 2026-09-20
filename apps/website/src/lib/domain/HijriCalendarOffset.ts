import { browser } from '$app/environment';
import { writable } from 'svelte/store';

/**
 * Key used to store in localStorage.
 */
const STORAGE_KEY = 'bönetider:preferences:hijri-calendar-offset';

/**
 * Maximum number of days the Hijri calendar can be shifted, in either direction.
 */
export const HIJRI_CALENDAR_OFFSET_RANGE = 7;

/**
 * Coerce a stored or user-supplied value into a whole number of days within range,
 * so a hand-edited localStorage entry can never reach the date formatter as
 * something like Infinity — which would produce an invalid date and throw.
 */
export function clampHijriCalendarOffset(value: number): number {
	if (!Number.isFinite(value)) {
		return 0;
	}
	return Math.min(
		Math.max(Math.trunc(value), -HIJRI_CALENDAR_OFFSET_RANGE),
		HIJRI_CALENDAR_OFFSET_RANGE
	);
}

/**
 * Holds the Hijri calendar offset (in days) as a svelte store.
 */
export const hijriCalendarOffset = writable<number>(getInitialValue());

/**
 * Save Hijri calendar offset to localStorage when it changes.
 */
hijriCalendarOffset.subscribe((value) => {
	if (browser) {
		localStorage.setItem(STORAGE_KEY, String(value));
	}
});

/**
 * Get the initial value for the Hijri calendar offset store.
 */
function getInitialValue(): number {
	if (browser) {
		const storedOffset = localStorage.getItem(STORAGE_KEY);
		if (storedOffset !== null) {
			const offset = Number(storedOffset);
			if (!Number.isNaN(offset)) {
				return clampHijriCalendarOffset(offset);
			}
		}
	}
	return 0;
}
