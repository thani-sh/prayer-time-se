import { browser } from '$app/environment';
import { writable } from 'svelte/store';

export type TimeFormat = 'h12' | 'h24';
export const TIME_FORMATS: readonly TimeFormat[] = ['h24', 'h12'];

/**
 * Key used to store in localStorage.
 */
const STORAGE_KEY = 'bönetider:preferences:timeFormat';

/**
 * Holds the selected time format as a svelte store.
 */
export const timeFormat = writable<TimeFormat>(getInitialValue());

/**
 * Save time format to localStorage when it changes.
 */
timeFormat.subscribe((value) => {
	if (browser && value) {
		localStorage.setItem(STORAGE_KEY, value);
	}
});

/**
 * Get the initial value for the time format store.
 */
function getInitialValue(): TimeFormat {
	if (browser) {
		const storedFormat = localStorage.getItem(STORAGE_KEY);
		if (storedFormat) {
			return storedFormat as TimeFormat;
		}
	}
	return 'h24';
}
