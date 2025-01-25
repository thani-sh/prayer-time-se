/**
 * Replaces åäö with aao and converts the text to lowercase.
 * This can be used as file names or machine readable names.
 */
export function toIdentifier(text) {
  return text.toLowerCase();
}

/**
 * Calls the callback for each combination of the two arrays.
 */
export async function combinations(a1, a2, callback) {
  for (const v1 of a1) {
    for (const v2 of a2) {
      await callback(v1, v2);
    }
  }
}

/**
 * Waits for the API request to be completed.
 */
export async function waitForAPIRequest(page, urlSegment) {
  return page.waitForResponse((res) => res.url().includes(urlSegment));
}
