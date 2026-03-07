import { integer, sqliteTable, text, primaryKey } from 'drizzle-orm/sqlite-core';

export const prayerTimes = sqliteTable(
	'prayer_times',
	{
		month: integer('month').notNull(),
		day: integer('day').notNull(),
		city: text('city').notNull(),
		fajr: integer('fajr').notNull(),
		sunrise: integer('sunrise').notNull(),
		dhuhr: integer('dhuhr').notNull(),
		asr: integer('asr').notNull(),
		maghrib: integer('maghrib').notNull(),
		isha: integer('isha').notNull()
	},
	(table) => ({
		pk: primaryKey({ columns: [table.month, table.day, table.city] }),
	})
);

export const metadata = sqliteTable(
	'metadata',
	{
		key: text('key').primaryKey(),
		value: text('value').notNull()
	}
);
