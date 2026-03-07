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

	const updatedDate = new Date(data.updated).toISOString().slice(0, 10);
	const currentDate = new Date().toISOString().slice(0, 10);

	if (updatedDate !== currentDate) {
		console.error(`❌ Error: API date is not today. Expected: ${currentDate}, Got: ${updatedDate}`);
		process.exit(1);
	}
	console.log('✅ API verify check passed! Data is fresh.');
}

verify().catch((err) => {
	console.error(err);
	process.exit(1);
});
