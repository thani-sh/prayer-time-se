package me.thanish.prayers.se.widget.nextprayer.store

import android.content.Context
import androidx.datastore.core.DataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.thanish.prayers.se.domain.PrayerTime
import me.thanish.prayers.se.domain.PrayerTimeCity

/**
 * NextPrayerTimeStore is a data store for getting next prayer time from system time.
 * This will emit the next prayer time every minute.
 */
class NextPrayerTimeStore(
    private val context: Context,
    private val interval: Long = 1_000
) : DataStore<PrayerTime> {
    override val data: Flow<PrayerTime>
        get() {
            return flow {
                while (true) {
                    val city = PrayerTimeCity.get(context)
                    emit(PrayerTime.getNextPrayer(context, city))
                    delay(interval)
                }
            }
        }

    override suspend fun updateData(transform: suspend (t: PrayerTime) -> PrayerTime): PrayerTime {
        TODO("Not yet implemented")
    }
}
