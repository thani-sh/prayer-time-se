package me.thanish.prayers.se.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PrayerTimeDao {
    @Query("SELECT * FROM prayer_times WHERE city = :city")
    fun forCity(city: String): List<PrayerTimeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(prayerTimes: List<PrayerTimeEntity>)

    @Query("SELECT value FROM metadata WHERE 'key' = :key")
    suspend fun getMetadata(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setMetadata(metadata: MetadataEntity)
}
