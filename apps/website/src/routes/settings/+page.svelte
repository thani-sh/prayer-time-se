<script lang="ts">
	import { CITIES, METHODS, type City, type Method } from '@thani-sh/prayer-time-se';
	import capitalize from 'lodash/capitalize';
	import { CodeIcon, MailIcon, CookieIcon } from 'lucide-svelte';
	import { city } from '$lib/domain/PrayerTimeCity';
	import { method } from '../../lib/domain/PrayerTimeMethod';
	import { TIME_FORMATS, timeFormat, type TimeFormat } from '$lib/domain/TimeFormat';
	import iphoneBadge from './iphone-badge.png';
	import androidBadge from './android-badge.png';
</script>

{#snippet Dropdown(
	label: string,
	options: readonly string[],
	selected: string,
	onSelect: (value: string) => void,
	formatLabel: (value: string) => string = capitalize
)}
	<fieldset class="fieldset my-2">
		<legend class="fieldset-legend">{label}</legend>
		<select
			class="select select-lg w-full"
			on:change={(e) => onSelect((e.target as HTMLSelectElement).value)}
		>
			{#each options as option}
				<option value={option} selected={selected === option}>{formatLabel(option)}</option>
			{/each}
		</select>
	</fieldset>
{/snippet}

<div class="flex flex-col w-full max-w-md mx-auto px-4 py-4 mb-32">
	<section>
		<h1 class="text-3xl font-light mb-2">Inställningar</h1>
		<div class="card bg-black/10 shadow-sm p-4">
			<p class="text-sm">
				De böner-tider som visas i appen beräknas med hjälp av dessa inställningar.
			</p>
			{@render Dropdown('Stad', CITIES, $city, (value) => city.set(value as City))}
			{@render Dropdown('Metod', METHODS, $method, (value) => method.set(value as Method))}
			{@render Dropdown(
				'Tidsformat',
				TIME_FORMATS,
				$timeFormat,
				(value) => timeFormat.set(value as TimeFormat),
				(value) => (value === 'h24' ? '24-timmars' : '12-timmars')
			)}
			<p class="text-sm mt-2">
				Denna app tillhandahåller exakta islamiska böner-tider som hämtas direkt från Islamiska
				Förbundets webbplats, som beräknar tider baserat på metoden som beskrivs här:
				<a href="https://www.islamiskaforbundet.se/bonetiders-kalla/" class="text-sky-400">
					bonetiders-kalla
				</a>
			</p>
		</div>
	</section>

	<section class="mt-8">
		<h1 class="text-3xl font-light mb-2">Om den här webbplatsen</h1>
		<div class="card bg-black/10 shadow-sm p-4">
			<p class="text-sm">
				Appen är öppen källkod och gratis att använda under MIT-licensen, och uppmuntrar alla att
				bidra eller dela feedback för att hjälpa till att förbättra den. Du kan utforska källkoden
				eller bidra via dess GitHub-arkivet.
			</p>

			<a href="https://github.com/thani-sh/prayer-time-se" class="btn btn-ghost justify-start mt-2">
				<CodeIcon class="w-4 h-4 mr-2" />
				Öppna GitHub-arkivet
			</a>

			<a href="mailto:contact@bönetider.nu" class="btn btn-ghost justify-start mt-2">
				<MailIcon class="w-4 h-4 mr-2" />
				Rapportera fel och feedback
			</a>

			<p class="text-sm mt-4">
				Din integritet är viktig för oss! Genom att använda den här appen godkänner du vår
				Integritetspolicy — du är välkommen att läsa den.
			</p>

			<a href="/docs/privacy" class="btn btn-ghost justify-start mt-2">
				<CookieIcon class="w-4 h-4 mr-2" />
				Öppna integritetspolicyn
			</a>
		</div>
	</section>

	<section class="mt-8">
		<h1 class="text-3xl font-light mb-2">Ladda ner appen</h1>
		<div class="card bg-black/10 shadow-sm p-4">
			<p class="text-sm">
				Installera appen på din telefon för att få snabb åtkomst till böner-tider och meddelanden.
			</p>

			<div class="flex flex-col sm:flex-row items-center justify-center mt-4 gap-4 grayscale">
				<a href="https://apps.apple.com/se/app/islamiska-b%C3%B6netider/id6741252950">
					<img src={iphoneBadge} alt="Ladda ner på App Store" />
				</a>
				<a href="https://play.google.com/store/apps/details?id=me.thanish.prayers.se">
					<img src={androidBadge} alt="Ladda ner på Google Play" />
				</a>
			</div>

			<p class="text-sm mt-4">Kom ihåg att betygsätta appen efter att ha provat den!</p>
		</div>
	</section>
</div>
