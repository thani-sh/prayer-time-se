<script lang="ts">
	import { onMount } from 'svelte';

	/**
	 * Compass page — points the way to the Qibla (Makkah).
	 *
	 * Design mirrors the native Android compass (QiblaCompass.kt): a dashed
	 * tick ring with a single N that rotates to track true north, plus a thin
	 * arrow with an arrowhead fixed on the dial at the Qibla bearing — when
	 * the arrow points straight up, the phone faces Makkah.
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

	// --- Compass dial geometry (SVG) ---
	const SIZE = 340;
	const C = SIZE / 2; // center
	const R = 108; // ring radius
	const CIRC = 2 * Math.PI * R;
	const SEG = CIRC / 60;
	const DASH = 4;
	// Colors pulled from the native app's Material theme on dark.
	const RING_COLOR = '#3c4353'; // outlineVariant-ish
	const ACCENT = '#ffb4ab'; // M3 primary (the native needle color)
	const dialRotation = $derived(heading === null ? 0 : -heading);
	const arrowRotation = $derived(qibla === null ? 0 : qibla);
</script>

<div class="flex flex-col items-center justify-center min-h-svh gap-5 px-6 pb-28">
	{#if pageState === 'permission'}
		<div class="flex flex-col items-center gap-4 text-center max-w-xs">
			<p class="text-lg font-medium">Kompassen behöver åtkomst till telefonens sensorer</p>
			<p class="text-sm opacity-60">Tryck på knappen och vrid sedan telefonen mot Qibla.</p>
			<button class="btn btn-primary btn-lg" onclick={enableCompass}>Aktivera kompass</button>
		</div>
	{:else}
		<!-- Rotating frame: dashed ring + N track true north; the Qibla arrow
		     sits on the dial at its bearing, so it points straight up when the
		     phone faces Makkah. -->
		<svg width={SIZE} height={SIZE} viewBox="0 0 {SIZE} {SIZE}">
			<g
				style="transform-origin: {C}px {C}px"
				style:transform="rotate({dialRotation}deg)"
			>
				<!-- dashed tick ring (60 segments, like the native app) -->
				<circle
					cx={C}
					cy={C}
					r={R}
					fill="none"
					stroke={RING_COLOR}
					stroke-width="15"
					stroke-dasharray="{DASH} {SEG - DASH}"
				/>
				<!-- north letter, just outside the ring -->
				<text
					x={C}
					y={C - R - 26}
					text-anchor="middle"
					fill={RING_COLOR}
					font-size="15"
					font-weight="600"
					font-family="Inter, sans-serif"
				>N</text>
				<!-- qibla arrow, fixed on the dial at the qibla bearing -->
				<g style:transform="rotate({arrowRotation}deg)" style="transform-origin: {C}px {C}px">
					<line x1={C} y1={C} x2={C} y2={C - R - 8} stroke={ACCENT} stroke-width="3" />
					<path d={`M ${C} ${C - R - 8} l -9 20 h 18 Z`} fill={ACCENT} />
				</g>
			</g>
			<!-- center pivot dot -->
			<circle cx={C} cy={C} r="4.5" fill="#0d1117" stroke={RING_COLOR} stroke-width="1.5" />
		</svg>

		<div class="text-center">
			{#if heading !== null}
				<p class="font-mono text-lg font-semibold tabular-nums" style="color: {ACCENT}">{heading}°</p>
			{:else if pageState === 'no-compass'}
				<p class="text-sm opacity-70 max-w-xs">
					Ingen kompass hittades på den här enheten — öppna sidan på en mobiltelefon för live-riktning.
				</p>
			{/if}
			{#if qibla !== null}
				<p class="mt-1 font-mono text-sm tabular-nums opacity-80">Qibla {Math.round(qibla)}°</p>
				<p class="text-xs opacity-50 mt-1">Vrid tills pilen pekar rakt upp</p>
			{/if}
			{#if locationNote}
				<p class="text-xs opacity-50 mt-3 max-w-xs mx-auto">{locationNote}</p>
			{/if}
		</div>
	{/if}
</div>
