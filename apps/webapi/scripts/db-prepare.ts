import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const dataDir = path.resolve(__dirname, '../../../data');
const outputFile = path.resolve(__dirname, '../values.sql');

async function main() {
	// 1. Read all city files in dataDir
	const files = fs.readdirSync(dataDir).filter(f => f.endsWith('.json'));

	const cityDataByDate: Record<string, Record<string, any>> = {};

	for (let m = 0; m < 12; m++) {
		for (let d = 0; d < 31; d++) {
			const dateKey = `${m}-${d}`;
			cityDataByDate[dateKey] = {};
		}
	}

	for (const file of files) {
		const [, city] = file.split('.');
		const content = fs.readFileSync(path.join(dataDir, file), 'utf-8');
		const json = JSON.parse(content);

		// JSON goes [month(0-11)][day(0-30)] => [...] times
		for (let m = 0; m < json.length; m++) {
			for (let d = 0; d < json[m].length; d++) {
				const dateKey = `${m}-${d}`;
				if (json[m][d]) {
					cityDataByDate[dateKey][city] = json[m][d];
				}
			}
		}
	}

	// 2. Build values.sql
	let sql = `DELETE FROM prayer_times;\n\n`;

	let inserts = [];
	let batchIndex = 0;
	
	for (let m = 0; m < 12; m++) {
		for (let d = 0; d < 31; d++) {
			const dateKey = `${m}-${d}`;
			const data = cityDataByDate[dateKey];
			
			for (const [city, times] of Object.entries(data)) {
				// times is an array: [fajr, sunrise, dhuhr, asr, maghrib, isha]
				inserts.push(`(${m}, ${d}, '${city}', ${times[0]}, ${times[1]}, ${times[2]}, ${times[3]}, ${times[4]}, ${times[5]})`);
			}
		}
	}

	const batchSize = 1000; // We can use larger batches now because the row size is much smaller
	for (let i = 0; i < inserts.length; i += batchSize) {
		const batch = inserts.slice(i, i + batchSize);
		sql += `INSERT INTO prayer_times (month, day, city, fajr, sunrise, dhuhr, asr, maghrib, isha) VALUES\n  ${batch.join(',\n  ')};\n\n`;
	}
	
	const nowStr = new Date().toISOString();
	sql += `DELETE FROM metadata WHERE key = 'last_updated';\n`;
	sql += `INSERT INTO metadata (key, value) VALUES ('last_updated', '${nowStr}');\n\n`;

	fs.writeFileSync(outputFile, sql, 'utf-8');
	console.log(`Successfully wrote ${outputFile}`);
}

main().catch(console.error);
