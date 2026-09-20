import { sv } from './sv';

export { sv };

/**
 * A key of the Swedish dictionary.
 */
export type TranslationKey = keyof typeof sv;

/**
 * Values used to replace `{name}` placeholders in a translation.
 */
export type TranslationParams = Record<string, string | number>;

/**
 * Translate a key, replacing `{name}` placeholders with the given params.
 */
export function t(key: TranslationKey, params?: TranslationParams): string {
	let value: string = sv[key];
	if (params) {
		for (const [name, param] of Object.entries(params)) {
			value = value.replaceAll(`{${name}}`, String(param));
		}
	}
	return value;
}
