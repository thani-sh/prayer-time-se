#!/usr/bin/env bash

set -e

echo "Generating values.sql..."
npx tsx scripts/db-prepare.ts

echo "Applying migrations to local D1 database..."
npx wrangler d1 migrations apply prayer-time-se --local

echo "Applying migrations to remote D1 database..."
npx wrangler d1 migrations apply prayer-time-se --remote

echo "Executing values.sql on local D1 database..."
npx wrangler d1 execute prayer-time-se --local --file=./values.sql

echo "Executing values.sql on remote D1 database..."
npx wrangler d1 execute prayer-time-se --remote --file=./values.sql

echo "Database update completed successfully."
