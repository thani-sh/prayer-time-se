import { CITIES, METHODS } from '@thani-sh/prayer-time-se';
import { and, eq } from 'drizzle-orm';
import { drizzle } from 'drizzle-orm/d1';
import { Hono } from 'hono';
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
	app.get('/v1/method/:method', async (c, next) => {
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
	app.get('/v1/method/:method/city/:city', async (c, next) => {
		const city = c.req.param('city');
		if (CITIES.indexOf(city as any) === -1) {
			c.status(404);
			return c.json({ error: 'City not found' });
		}
		await next();
	});

	/**
	 * API endpoint to get prayer times for a year
	 */
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
