import { CITIES, METHODS } from '@thani-sh/prayer-time-se';
import { and, eq } from 'drizzle-orm';
import { drizzle } from 'drizzle-orm/d1';
import { Hono } from 'hono';
import { cache } from 'hono/cache';
import { Env } from '../../cf-types';
import { metadata, prayerTimes } from '../db/schema';

/**
 * Function to register the v1 API endpoints and middleware
 */
export function registerV1(app: Hono<{ Bindings: Env }>) {
	/**
	 * API endpoint to check if the server is up
	 */
	app.get('/v1/ping', (c) => {
		return c.json({ message: 'pong' });
	});

	/**
	 * API endpoint to get the API's current version and last updated date
	 */
	app.get('/v1/version', async (c) => {
		const db = drizzle(c.env.db);
		const row = await db.select().from(metadata).where(eq(metadata.key, 'last_updated')).get();
		const updated = row?.value || 'unknown';
		return c.json({ updated });
	});

	/**
	 * API endpoint to get the list of methods
	 */
	app.get('/v1/methods', (c) => {
		return c.json(METHODS);
	});

	/**
	 * Middleware to check if the method is valid
	 */
	app.use('/v1/method/:method/*', async (c, next) => {
		const method = c.req.param('method');
		if (METHODS.indexOf(method as any) === -1) {
			c.status(404);
			return c.json({ error: 'Method not found' });
		}
		await next();
	});

	/**
	 * API endpoint to get the list of cities
	 */
	app.get('/v1/method/:method/cities', (c) => {
		return c.json(CITIES);
	});

	/**
	 * Middleware to check if the city is valid
	 */
	app.use('/v1/method/:method/city/:city/*', async (c, next) => {
		const city = c.req.param('city');
		if (CITIES.indexOf(city as any) === -1) {
			c.status(404);
			return c.json({ error: 'City not found' });
		}
		await next();
	});

	/**
	 * API endpoint to get prayer times for a year
	 *
	 * The full-year dataset is immutable for the year, so responses are cached
	 * at the edge with the Cloudflare Cache API. Repeat requests (e.g. the
	 * mobile apps re-syncing after a version bump) never hit D1.
	 *
	 * The cache key is scoped to the data version (last_updated): a mid-year
	 * data correction bumps it, so the next request misses and refetches from
	 * D1 instead of serving the previous payload for up to 24h. The version
	 * read is a single-key metadata lookup, far cheaper than the 366-row
	 * times query it gates. `wait: true` stores the response before the first
	 * client returns, so a burst of concurrent misses right after a version
	 * bump can't stampede D1.
	 */
	app.use(
		'/v1/method/*/city/*/times',
		cache({
			cacheName: 'prayer-times-full-year',
			cacheControl: 'public, max-age=86400',
			keyGenerator: async (c) => {
				const db = drizzle(c.env.db);
				const row = await db
					.select()
					.from(metadata)
					.where(eq(metadata.key, 'last_updated'))
					.get();
				const version = row?.value || 'unknown';
				// Cache API keys must be fully-qualified URLs, so scope by
				// path + data version via a query param. Client query strings
				// are dropped so cache-busting params and method-case variants
				// share one entry per city.
				const url = new URL(c.req.url);
				url.search = '';
				url.searchParams.set('v', version);
				return url.toString();
			},
			wait: true
		})
	);
	app.get('/v1/method/:method/city/:city/times', async (c) => {
		const city = c.req.param('city');
		const db = drizzle(c.env.db);
		const rows = await db.select().from(prayerTimes).where(eq(prayerTimes.city, city)).all();
		if (!rows || rows.length === 0) {
			c.status(404);
			return c.json({ error: 'Data not found' });
		}
		// Map SQL rows back to the 12-month format array.
		const dataset: any[][] = Array.from({ length: 12 }, () => []);
		for (const row of rows) {
			dataset[row.month][row.day] = [row.fajr, row.sunrise, row.dhuhr, row.asr, row.maghrib, row.isha];
		}
		c.header('content-type', 'application/json');
		return c.json(dataset);
	});

	/**
	 * API endpoint to get prayer times for a date
	 */
	app.get('/v1/method/:method/city/:city/times/:date', async (c) => {
		const city = c.req.param('city');
		const dateObj = new Date(c.req.param('date'));
		const [m, d] = [dateObj.getMonth(), dateObj.getDate() - 1];
		const db = drizzle(c.env.db);
		const row = await db
			.select()
			.from(prayerTimes)
			.where(and(eq(prayerTimes.month, m), eq(prayerTimes.day, d), eq(prayerTimes.city, city)))
			.get();
		if (!row) {
			c.status(404);
			return c.json({ error: 'Data not found for this date' });
		}
		const times = [row.fajr, row.sunrise, row.dhuhr, row.asr, row.maghrib, row.isha];
		c.header('content-type', 'application/json');
		return c.json(times);
	});
}
