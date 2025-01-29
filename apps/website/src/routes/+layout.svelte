<script lang="ts">
	import '../style.css';
	import { page } from '$app/state';
	import { onNavigate } from '$app/navigation';
	import { CalendarIcon, CompassIcon, SettingsIcon } from 'lucide-svelte';

	const routes = [
		{ path: '/', label: 'Schema', icon: CalendarIcon },
		{ path: '/compass', label: 'Kompass', icon: CompassIcon },
		{ path: '/settings', label: 'Inställningar', icon: SettingsIcon }
	];

	/**
	 * Start a view transition when navigating between routes.
	 */
	onNavigate((navigation) => {
		if (!document.startViewTransition) {
			return;
		}
		return new Promise((resolve) => {
			document.startViewTransition(async () => {
				resolve();
				await navigation.complete;
			});
		});
	});
</script>

<slot />

<div class="dock dock-xl">
	{#each routes as { path, label, icon }}
		<a href={path} class={page.url.pathname === path ? 'opacity-80' : 'opacity-40'}>
			<span
				class={page.url.pathname === path
					? 'px-4 py-1 transition rounded-full bg-sky-500/25'
					: 'px-4 py-1 transition rounded-full'}
			>
				<svelte:component this={icon} class="w-4 h-4" />
			</span>
			<span class="dock-label">{label}</span>
		</a>
	{/each}
</div>
