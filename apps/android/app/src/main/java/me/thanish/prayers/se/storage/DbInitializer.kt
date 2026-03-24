package me.thanish.prayers.se.storage

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DbInitializer {
    private const val LAST_UPDATED_KEY = "last_updated"
    private const val LAST_UPDATED_VAL = "2026-03-07T00:00:00.000Z"

    suspend fun initialize(context: Context) {
        val database = AppDatabase.getDatabase(context)
        val dao = database.prayerTimeDao()

        val lastUpdated = dao.getMetadata(LAST_UPDATED_KEY)
        if (lastUpdated != null) {
            // Already initialized
            return
        }

        withContext(Dispatchers.IO) {
            val assets = context.assets.list("values") ?: return@withContext
            val gson = Gson()
            val entities = mutableListOf<PrayerTimeEntity>()

            for (fileName in assets) {
                if (!fileName.endsWith(".json")) continue
                
                // Format is method.city.json
                val parts = fileName.split(".")
                if (parts.size != 3) continue
                val city = parts[1]

                val content = context.assets.open("values/$fileName").bufferedReader().use { it.readText() }
                val json = gson.fromJson(content, Array<Array<Array<Int>>>::class.java)

                for (m in json.indices) {
                    for (d in json[m].indices) {
                        val times = json[m][d]
                        entities.add(
                            PrayerTimeEntity(
                                month = m,
                                day = d,
                                city = city,
                                fajr = times[0],
                                sunrise = times[1],
                                dhuhr = times[2],
                                asr = times[3],
                                maghrib = times[4],
                                isha = times[5]
                            )
                        )
                    }
                }
                
                // Batch insert to avoid huge memory usage
                if (entities.size > 5000) {
                    dao.insertAll(entities)
                    entities.clear()
                }
            }

            if (entities.isNotEmpty()) {
                dao.insertAll(entities)
            }

            dao.setMetadata(MetadataEntity(LAST_UPDATED_KEY, LAST_UPDATED_VAL))
        }
    }
}
