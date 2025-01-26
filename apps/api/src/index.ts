import { Hono } from 'hono';
import { cors } from 'hono/cors';
import { etag } from 'hono/etag';
import { logger } from 'hono/logger';
import { registerV1 } from './routes/v1';
import { Env } from '../cf-types';

const app = new Hono<{ Bindings: Env }>();

// Middleware
app.use(cors());
app.use(etag());
app.use(logger());

// API Routes
registerV1(app);

export default app;
