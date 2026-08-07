package me.thanish.prayers.se.widget.nextprayer.store

import android.content.Context
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.thanish.prayers.se.domain.PrayerTime
import me.thanish.prayers.se.domain.PrayerTimeCity
import me.thanish.prayers.se.domain.PrayerTimeMethod

/**
 * NextPrayerTimeStore is a data store for getting next prayer time from system time.
 */
class NextPrayerTimeStore(
    private val context: Context
) : DataStore<PrayerTime> {
    override val data: Flow<PrayerTime>
        get() {
            return flow {
                val method = PrayerTimeMethod.get(context)
                val city = PrayerTimeCity.get(context)
                emit(PrayerTime.getNextPrayer(context, method, city))
            }
        }

    override suspend fun updateData(transform: suspend (t: PrayerTime) -> PrayerTime): PrayerTime {
        TODO("Not yet implemented")
    }
}
