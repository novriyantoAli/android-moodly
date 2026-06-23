package net.coblos.moodly.domain.repository

import kotlinx.coroutines.flow.Flow
import net.coblos.moodly.domain.model.MoodEntry

interface MoodRepository {
    fun getMoodHistory(): Flow<List<MoodEntry>>
    suspend fun addMood(mood: MoodEntry): Result<Unit>
    suspend fun deleteMood(id: Long): Result<Unit>
    suspend fun syncMoods(): Result<Unit>
}
