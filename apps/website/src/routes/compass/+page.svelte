<script lang="ts">
	import { onMount } from 'svelte';

	/**
	 * Compass page — points the way to the Qibla (Makkah).
	 *
	 * Sources:
	 * - Location: Geolocation API. Falls back to Stockholm coordinates
	 *   (same default as the native iOS app) when the user declines.
	 * - Heading: deviceorientation events. iOS exposes webkitCompassHeading
	 *   (degrees, needs a permission tap since iOS 13). Android/others expose
	 *   absolute alpha/beta/gamma — converted with the W3C worked example
	 *   (upright phone) or (360 - alpha) (flat phone).
	 * - Desktop browsers have no magnetometer: we still show the bearing.
	 */

	const MAKKAH = { lat: 21.4225, lon: 39.8262 };
	const FALLBACK = { lat: 59.3327, lon: 18.0656, name: 'Stockholm' };

	/** Safari-only extensions not in the TS DOM types. */
	interface IOSDeviceOrientationEvent extends DeviceOrientationEvent {
		webkitCompassHeading?: number;
		webkitCompassAccuracy?: number;
	}
	const OrientationEvent = (typeof DeviceOrientationEvent !== 'undefined' ? DeviceOrientationEvent : null) as
		| (typeof DeviceOrientationEvent & { requestPermission?: () => Promise<'granted' | 'denied'> })
		| null;

	type PageState = 'permission' | 'starting' | 'running' | 'no-compass' | 'unsupported';

	let pageState = $state<PageState>('starting');
	let heading = $state<number | null>(null); // smoothed, degrees
	let qibla = $state<number | null>(null); // bearing from user location, degrees
	let locationNote = $state('');

	const toRad = (d: number) => (d * Math.PI) / 180;
	const toDeg = (r: number) => (r * 180) / Math.PI;
	const wrap360 = (a: number) => ((a % 360) + 360) % 360;

	/** Great-circle initial bearing from (lat, lon) to Makkah, degrees [0, 360). */
	function qiblaBearing(lat: number, lon: number): number {
		const lat1 = toRad(lat);
		const lon1 = toRad(lon);
		const lat2 = toRad(MAKKAH.lat);
		const lon2 = toRad(MAKKAH.lon);
		const y = Math.sin(lon2 - lon1) * Math.cos(lat2);
		const x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
		return wrap360(toDeg(Math.atan2(y, x)));
	}

	/**
	 * W3C device-orientation worked example: compass heading of the screen
	 * normal's horizontal component. Works for a tilted/upright phone;
	 * degenerates only when the phone is perfectly flat (handled by caller).
	 */
	function specHeading(alpha: number, beta: number, gamma: number): number {
		const a = toRad(alpha);
		const b = toRad(beta);
		const g = toRad(gamma);
		const cA = Math.cos(a);
		const sA = Math.sin(a);
		const cB = Math.cos(b);
		const sB = Math.sin(b);
		const cG = Math.cos(g);
		const sG = Math.sin(g);
		const rA = -cA * sG - sA * sB * cG;
		const rB = -sA * sG + cA * sB * cG;
		return wrap360(toDeg(Math.atan2(rA, rB)));
	}

	function headingFromEvent(e: DeviceOrientationEvent): number | null {
		// iOS: webkitCompassHeading is already the compass heading in degrees.
		const iosHeading = (e as IOSDeviceOrientationEvent).webkitCompassHeading;
		if (typeof iosHeading === 'number') {
			return wrap360(iosHeading);
		}
		// Android etc: absolute alpha/beta/gamma.
		if (e.absolute === true && e.alpha !== null && e.beta !== null && e.gamma !== null) {
			// Roughly flat (screen up): alpha is the rotation around the
			// vertical axis, heading = 360 - alpha.
			if (Math.abs(e.beta) < 8) {
				return wrap360(360 - e.alpha);
			}
			return specHeading(e.alpha, e.beta, e.gamma);
		}
		return null;
	}

	/** Low-pass the noisy raw heading into the smoothed `heading` state. */
	let rawHeading: number | null = null;
	let smoothHeading: number | null = null;
	let rafId = 0;
	function smoothLoop() {
		if (rawHeading !== null) {
			let target = rawHeading;
			if (smoothHeading === null) {
				smoothHeading = target;
			} else {
				// Shortest-path interpolation between smooth and target.
				let diff = wrap360(target - smoothHeading);
				if (diff > 180) diff -= 360;
				smoothHeading = wrap360(smoothHeading + diff * 0.15);
			}
			heading = Math.round(smoothHeading);
		}
		rafId = requestAnimationFrame(smoothLoop);
	}

	function startListening() {
		pageState = 'running';
		rawHeading = null;
		smoothHeading = null;
		const onOrientation = (e: DeviceOrientationEvent) => {
			const h = headingFromEvent(e);
			if (h !== null) rawHeading = h;
		};
		window.addEventListener('deviceorientationabsolute', onOrientation);
		window.addEventListener('deviceorientation', onOrientation);
		// No compass data within 3 s (desktop, or sensors off): keep static view.
		setTimeout(() => {
			if (rawHeading === null) {
				window.removeEventListener('deviceorientationabsolute', onOrientation);
				window.removeEventListener('deviceorientation', onOrientation);
				pageState = 'no-compass';
			}
		}, 3000);
	}

	function resolveLocation() {
		if (!navigator.geolocation) {
			useFallback();
			return;
		}
		navigator.geolocation.getCurrentPosition(
			(pos) => {
				qibla = qiblaBearing(pos.coords.latitude, pos.coords.longitude);
				locationNote = '';
			},
			() => useFallback(),
			{ timeout: 8000, maximumAge: 60000 }
		);
	}

	function useFallback() {
		qibla = qiblaBearing(FALLBACK.lat, FALLBACK.lon);
		locationNote = `Ungefärlig riktning från ${FALLBACK.name}. Aktivera platstjänster för bättre noggrannhet.`;
	}

	async function enableCompass() {
		try {
			if (OrientationEvent?.requestPermission) {
				const result = await OrientationEvent.requestPermission();
				if (result !== 'granted') return;
			}
		} catch {
			// Older browsers: no permission API, events just start.
		}
		startListening();
	}

	onMount(() => {
		if (typeof DeviceOrientationEvent === 'undefined') {
			pageState = 'unsupported';
		} else if (OrientationEvent?.requestPermission) {
			// iOS 13+ (and newer Chrome): permission needs a user tap.
			pageState = 'permission';
		} else {
			startListening();
		}
		rafId = requestAnimationFrame(smoothLoop);
		resolveLocation();
		return () => cancelAnimationFrame(rafId);
	});
</script>

<div class="flex flex-col items-center justify-center min-h-svh gap-6 px-6 pb-28">
	{#if pageState === 'permission'}
		<div class="flex flex-col items-center gap-4 text-center max-w-xs">
			<p class="text-lg font-medium">Kompassen behöver åtkomst till telefonens sensorer</p>
			<p class="text-sm opacity-60">Tryck på knappen och vrid sedan telefonen mot Qibla.</p>
			<button class="btn btn-primary btn-lg" onclick={enableCompass}>Aktivera kompass</button>
		</div>
	{:else}
		<!-- Compass dial: rotates so North aligns with the device heading.
		     The Qibla marker is fixed on the dial, so when it points to the top
		     (12 o'clock), the phone is facing the Qibla. -->
		<div class="relative w-72 h-72" style="transform: rotate({-(heading ?? 0)}deg); transition: transform 0.15s linear">
			<!-- outer ring + cardinal ticks -->
			<div class="absolute inset-0 rounded-full border-2 border-sky-400/40 bg-slate-900/60"></div>
			<div class="absolute inset-3 rounded-full border border-slate-600/60"></div>
			{#each [0, 45, 90, 135, 180, 225, 270, 315] as deg}
				<div
					class="absolute left-1/2 top-1/2 w-px h-3 bg-slate-500/70"
					style="transform: translate(-50%, -100%) rotate({deg}deg) translateY(-118px)"
				></div>
			{/each}
			<span class="absolute left-1/2 top-3 -translate-x-1/2 text-lg font-bold text-sky-300">N</span>
			<span class="absolute right-4 top-1/2 -translate-y-1/2 text-sm font-semibold text-slate-400">Ö</span>
			<span class="absolute bottom-3 left-1/2 -translate-x-1/2 text-sm font-semibold text-slate-500">S</span>
			<span class="absolute left-4 top-1/2 -translate-y-1/2 text-sm font-semibold text-slate-400">V</span>
			<!-- Qibla marker on the dial -->
			{#if qibla !== null}
				<div
					class="absolute left-1/2 top-1/2 w-1 h-24 rounded-full bg-emerald-400"
					style="transform: translate(-50%, -100%) rotate({qibla}deg) translateY(-84px)"
				></div>
			{/if}
		</div>

		<!-- fixed lubber line (where the phone points) -->
		<div class="w-0 h-0 -mt-2 border-l-8 border-r-8 border-t-[10px] border-l-transparent border-r-transparent border-t-sky-300"></div>

		<div class="text-center">
			{#if heading !== null}
				<p class="font-mono text-4xl font-bold tabular-nums">{heading}°</p>
				<p class="text-sm opacity-60">Riktning</p>
			{:else if pageState === 'no-compass'}
				<p class="text-sm opacity-70 max-w-xs">
					Ingen kompass hittades på den här enheten — öppna sidan på en mobiltelefon för live-riktning.
				</p>
			{/if}
			{#if qibla !== null}
				<p class="mt-3 font-mono text-2xl font-semibold tabular-nums text-emerald-300">Qibla {qibla}°</p>
				<p class="text-xs opacity-50 mt-1">Vrid tills den gröna linjen pekar rakt upp</p>
			{/if}
			{#if locationNote}
				<p class="text-xs opacity-50 mt-3 max-w-xs mx-auto">{locationNote}</p>
			{/if}
		</div>
	{/if}
</div>
