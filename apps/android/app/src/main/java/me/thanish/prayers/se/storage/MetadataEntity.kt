package me.thanish.prayers.se.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metadata")
data class MetadataEntity(
    @PrimaryKey val key: String,
    val value: String
)
