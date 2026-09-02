package me.thanish.prayers.se

import android.app.Application
import android.net.http.HttpResponseCache
import java.io.File

/**
 * Application entry point.
 *
 * Installs a disk-backed HTTP response cache so that API calls made through
 * HttpURLConnection honor the Cache-Control headers served by the API (e.g.
 * the full-year prayer times endpoint). Version-mismatch syncs bypass this
 * cache (see PrayerTimeRepository.fetchFresh) so a data change is always
 * fetched from the network; the cache serves repeat reads and guards against
 * transient network failures in between.
 *
 * Note: [android.net.http.HttpResponseCache] is deprecated since API 23 but
 * remains the framework-supported cache for HttpURLConnection, so the
 * deprecation warning is intentional.
 */
@Suppress("DEPRECATION")
class PrayersApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        try {
            HttpResponseCache.install(File(cacheDir, "http_cache"), HTTP_CACHE_SIZE_BYTES)
        } catch (_: Exception) {
            // Caching is best-effort; the app works fine without it.
        }
    }

    private companion object {
        const val HTTP_CACHE_SIZE_BYTES = 10L * 1024 * 1024 // 10 MB
    }
}
