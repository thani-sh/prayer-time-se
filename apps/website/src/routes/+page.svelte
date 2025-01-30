<script lang="ts">
	import { getPrayerTimes } from '@thani-sh/prayer-time-se';
	import { city } from '$lib/domain/PrayerTimeCity';
	import { method } from '$lib/domain/PrayerTimeMethod';

	const hijriDateFormatter = new Intl.DateTimeFormat('ar-TN-u-ca-islamic', {
		year: 'numeric',
		month: 'long',
		day: 'numeric',
		era: 'short'
	});

	const date = new Date();

	function getDateString() {
		return date.toLocaleDateString();
	}

	function getHijriDateString() {
		return hijriDateFormatter.format(date);
	}
</script>

{#snippet TableRow(name: string, hour: number, minute: number)}
	<div class="flex flex-row py-4 space-x-6 items-center justify-center">
		<div class="text-lg w-1/2 text-right text-gray-300">{name}</div>
		<div class="text-lg w-1/2 text-gray-300">
			{String(hour).padStart(2, '0')}:{String(minute).padStart(2, '0')}
		</div>
	</div>
{/snippet}

<div class="flex flex-col items-center justify-center h-screen w-full">
	<div class="flex flex-col items-center mb-16">
		<h1 class="text-5xl font-bold mb-2">Bönetider</h1>
		<p class="text-md text-gray-500">{getDateString()} - {getHijriDateString()}</p>
	</div>

	{#await getPrayerTimes(date, $method, $city) then prayerTimes}
		<div class="w-3xs divide-y divide-gray-800 mb-16">
			{@render TableRow('Fajr', prayerTimes.fajr.hour, prayerTimes.fajr.minute)}
			{@render TableRow('Shuruk', prayerTimes.sunrise.hour, prayerTimes.sunrise.minute)}
			{@render TableRow('Dhohr', prayerTimes.dhuhr.hour, prayerTimes.dhuhr.minute)}
			{@render TableRow('Asr', prayerTimes.asr.hour, prayerTimes.asr.minute)}
			{@render TableRow('Maghrib', prayerTimes.maghrib.hour, prayerTimes.maghrib.minute)}
			{@render TableRow('Isha', prayerTimes.isha.hour, prayerTimes.isha.minute)}
		</div>
	{:catch error}
		<p>Error: {error.message}</p>
	{/await}
</div>
