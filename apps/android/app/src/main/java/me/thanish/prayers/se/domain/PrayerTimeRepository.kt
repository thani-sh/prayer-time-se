package me.thanish.prayers.se.domain

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.thanish.prayers.se.storage.AppDatabase
import me.thanish.prayers.se.storage.MetadataEntity
import me.thanish.prayers.se.storage.PrayerTimeEntity
import java.net.URL

object PrayerTimeRepository {
    private const val TAG = "PrayerTimeRepository"
    private const val BASE_URL = "https://api.xn--bnetider-n4a.nu"
    private const val LAST_UPDATED_KEY = "last_updated"

    suspend fun syncIfNeeded(
        context: Context,
        onProgress: (Int, Int) -> Unit = { _, _ -> }
    ) {
        try {
            val remoteVersion = getRemoteVersion()
            val localVersion = getLocalVersion(context)
            
            if (remoteVersion != localVersion) {
                Log.i(TAG, "New version available: $remoteVersion (local: $localVersion). Syncing...")
                syncAllPrayerTimes(context, remoteVersion, onProgress)
            } else {
                Log.i(TAG, "Prayer times are up to date ($localVersion)")
                // Still call onProgress once to indicate completion if needed, 
                // but here it's already done.
                onProgress(1, 1)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sync prayer times", e)
            throw e
        }
    }

    private suspend fun getRemoteVersion(): String = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/v1/version")
        val json = url.readText()
        val map = Gson().fromJson(json, Map::class.java)
        map["updated"] as String
    }

    private suspend fun getLocalVersion(context: Context): String? {
        val db = AppDatabase.getDatabase(context)
        return db.prayerTimeDao().getMetadata(LAST_UPDATED_KEY)
    }

    suspend fun syncAllPrayerTimes(
        context: Context,
        version: String,
        onProgress: (Int, Int) -> Unit = { _, _ -> }
    ) = withContext(Dispatchers.IO) {
        val allEntities = mutableListOf<PrayerTimeEntity>()
        val total = PrayerTimeMethod.entries.size * PrayerTimeCity.entries.size
        var current = 0
        
        for (method in PrayerTimeMethod.entries) {
            for (city in PrayerTimeCity.entries) {
                try {
                    val url = URL("$BASE_URL/v1/method/$method/city/$city/times")
                    val json = url.readText()
                    val dataset = Gson().fromJson(json, Array<Array<Array<Int>>>::class.java)

                    for (m in dataset.indices) {
                        for (d in dataset[m].indices) {
                            val times = dataset[m][d]
                            allEntities.add(
                                PrayerTimeEntity(
                                    month = m,
                                    day = d,
                                    city = city.name,
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
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to fetch times for $city ($method)", e)
                    // If one city fails, we continue with others
                } finally {
                    current++
                    onProgress(current, total)
                }
            }
        }

        if (allEntities.isNotEmpty()) {
            val db = AppDatabase.getDatabase(context)
            val dao = db.prayerTimeDao()
            dao.insertAll(allEntities)
            dao.setMetadata(MetadataEntity(LAST_UPDATED_KEY, version))
            
            // Clear in-memory cache in PrayerTimeData
            PrayerTimeData.clearCache()
            Log.i(TAG, "Successfully synced ${allEntities.size} prayer time entries ($version)")
        }
    }
}
