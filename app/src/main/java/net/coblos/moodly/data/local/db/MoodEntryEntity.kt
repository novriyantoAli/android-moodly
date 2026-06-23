package net.coblos.moodly.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val emotionScore: Int,
    val note: String,
    val photoUri: String?,
    val createdAt: Long,
    val isSynced: Boolean = false
)
