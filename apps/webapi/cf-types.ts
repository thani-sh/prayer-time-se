import type { R2Bucket } from '@cloudflare/workers-types';

export interface Env {
	BUCKET: R2Bucket;
}
