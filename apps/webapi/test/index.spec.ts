// test/index.spec.ts
import { env, createExecutionContext, waitOnExecutionContext, SELF } from 'cloudflare:test';
import { describe, it, expect } from 'vitest';
import worker from '../src/index';

// For now, you'll need to do something like this to get a correctly-typed
// `Request` to pass to `worker.fetch()`.
const IncomingRequest = Request<unknown, IncomingRequestCfProperties>;

describe('Prayer Time SE API', () => {
	describe('/v1/ping', () => {
		it('responds with pong (unit style)', async () => {
			const request = new IncomingRequest('http://example.com/v1/ping');
			const ctx = createExecutionContext();
			const response = await worker.fetch(request, env, ctx);
			await waitOnExecutionContext(ctx);
			expect(response.status).toBe(200);
			expect(await response.json()).toEqual({ message: 'pong' });
		});

		it('responds with pong (integration style)', async () => {
			const response = await SELF.fetch('https://example.com/v1/ping');
			expect(response.status).toBe(200);
			expect(await response.json()).toEqual({ message: 'pong' });
		});
	});

	describe('/v1/version', () => {
		it('responds with version info', async () => {
			const response = await SELF.fetch('https://example.com/v1/version');
			expect(response.status).toBe(200);
			const data = await response.json();
			expect(data).toHaveProperty('updated');
			expect(typeof data.updated).toBe('string');
		});
	});

	describe('/v1/methods', () => {
		it('responds with list of methods', async () => {
			const response = await SELF.fetch('https://example.com/v1/methods');
			expect(response.status).toBe(200);
			const data = await response.json();
			expect(Array.isArray(data)).toBe(true);
			expect(data.length).toBeGreaterThan(0);
		});
	});

	describe('/v1/method/:method/cities', () => {
		it('responds with list of cities for any method', async () => {
			const response = await SELF.fetch('https://example.com/v1/method/invalid/cities');
			expect(response.status).toBe(200);
			const data = await response.json();
			expect(Array.isArray(data)).toBe(true);
		});
	});

	describe('root path', () => {
		it('responds with 404 for root path', async () => {
			const response = await SELF.fetch('https://example.com');
			expect(response.status).toBe(404);
		});
	});
});
