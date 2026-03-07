import type { D1Database } from '@cloudflare/workers-types';

export interface Env {
	db: D1Database;
}
