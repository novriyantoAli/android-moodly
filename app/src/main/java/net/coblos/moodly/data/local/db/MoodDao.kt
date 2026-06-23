package net.coblos.moodly.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries ORDER BY createdAt DESC")
    fun getAllMoods(): Flow<List<MoodEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: MoodEntryEntity): Long

    @Query("DELETE FROM mood_entries WHERE id = :id")
    suspend fun deleteMood(id: Long)

    @Query("SELECT * FROM mood_entries WHERE isSynced = 0")
    suspend fun getUnsyncedMoods(): List<MoodEntryEntity>
}
