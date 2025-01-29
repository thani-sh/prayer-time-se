import { browser } from '$app/environment';
import type { City } from '@thani-sh/prayer-time-se';
import { writable } from 'svelte/store';

/**
 * Key used to store in localStorage.
 */
const STORAGE_KEY = 'preferences:city';

/**
 * Holds the selected city as a svelte store.
 */
export const city = writable<City>(getInitialValue());

/**
 * Save city to localStorage when it changes.
 */
city.subscribe((value) => {
	if (browser && value) {
		localStorage.setItem(STORAGE_KEY, value);
	}
});

/**
 * Get the initial value for the city store.
 */
function getInitialValue(): City {
	if (browser) {
		const storedCity = localStorage.getItem(STORAGE_KEY);
		if (storedCity) {
			return storedCity as City;
		}
	}
	return 'stockholm';
}
