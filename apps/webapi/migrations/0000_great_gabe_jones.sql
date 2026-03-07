CREATE TABLE `prayer_times` (
	`month` integer NOT NULL,
	`day` integer NOT NULL,
	`city` text NOT NULL,
	`fajr` integer NOT NULL,
	`sunrise` integer NOT NULL,
	`dhuhr` integer NOT NULL,
	`asr` integer NOT NULL,
	`maghrib` integer NOT NULL,
	`isha` integer NOT NULL,
	PRIMARY KEY(`month`, `day`, `city`)
);
