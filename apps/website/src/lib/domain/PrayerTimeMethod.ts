import { browser } from '$app/environment';
import type { Method } from '@thani-sh/prayer-time-se';
import { writable } from 'svelte/store';

/**
 * Key used to store in localStorage.
 */
const STORAGE_KEY = 'preferences:method';

/**
 * Holds the selected method as a svelte store.
 */
export const method = writable<Method>(getInitialValue());

/**
 * Save city to localStorage when it changes.
 */
method.subscribe((value) => {
	if (browser && value) {
		localStorage.setItem(STORAGE_KEY, value);
	}
});

/**
 * Get the initial value for the method store.
 */
function getInitialValue(): Method {
	if (browser) {
		const storedCity = localStorage.getItem(STORAGE_KEY);
		if (storedCity) {
			return storedCity as Method;
		}
	}
	return 'islamiskaforbundet';
}
