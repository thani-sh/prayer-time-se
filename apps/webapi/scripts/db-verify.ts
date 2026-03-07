async function verify() {
	const url = 'https://api.bönetider.nu/v1/version';
	console.log('Fetching', url);

	const res = await fetch(url);
	if (!res.ok) {
		console.error('❌ HTTP Error:', res.status);
		process.exit(1);
	}

	const data = await (res.json() as Promise<{ updated: string }>);
	console.log('Received:', data);

	const updatedTime = new Date(data.updated).getTime();
	const diffMinutes = (Date.now() - updatedTime) / (1000 * 60);

	if (isNaN(diffMinutes) || diffMinutes > 15) {
		console.error('❌ Error: API date is not recent or invalid. Diff:', diffMinutes, 'min');
		process.exit(1);
	}
	console.log('✅ API verify check passed! Data is fresh.');
}

verify().catch((err) => {
	console.error(err);
	process.exit(1);
});
