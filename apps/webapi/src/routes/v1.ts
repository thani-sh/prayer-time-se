import { Hono } from 'hono';
import { CITIES, METHODS } from '@thani-sh/prayer-time-se';
import { Env } from '../../cf-types';

/**
 * Function to convert a readable stream to an object
 */
async function streamToObject(stream: ReadableStream) {
	const text = await new Response(stream).text();
	return JSON.parse(text);
}

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
	 * API endpoint to check if the server is up
	 */
	app.get('/v1/version', (c) => {
		return c.json({ updated: '2025-01-26' });
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
			c.json({ error: 'Method not found' });
			return;
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
			c.json({ error: 'City not found' });
			return;
		}
		await next();
	});

	/**
	 * API endpoint to get prayer times for a year
	 */
	app.get('/v1/method/:method/city/:city/times', async (c) => {
		const method = c.req.param('method');
		const city = c.req.param('city');
		const object = await c.env.BUCKET.get(`${method}.${city}.json`);
		if (object === null) {
			c.status(404);
			return c.json({ error: 'Data not found' });
		}
		c.header('content-type', 'application/json');
		return c.body(object.body);
	});

	/**
	 * API endpoint to get prayer times for a date
	 */
	app.get('/v1/method/:method/city/:city/times/:date', async (c) => {
		const method = c.req.param('method');
		const city = c.req.param('city');
		const date = new Date(c.req.param('date'));
		const object = await c.env.BUCKET.get(`${method}.${city}.json`);
		if (object === null) {
			c.status(404);
			return c.json({ error: 'Data not found' });
		}
		const dataset = await streamToObject(object.body);
		const [m, d] = [date.getMonth(), date.getDate() - 1];
		c.header('content-type', 'application/json');
		return c.json(dataset[m][d]);
	});
}
