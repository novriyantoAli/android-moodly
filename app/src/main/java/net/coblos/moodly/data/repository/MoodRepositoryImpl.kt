package net.coblos.moodly.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.coblos.moodly.data.local.db.MoodDao
import net.coblos.moodly.data.local.db.MoodEntryEntity
import net.coblos.moodly.data.remote.api.MoodApiService
import net.coblos.moodly.domain.model.Emotion
import net.coblos.moodly.domain.model.MoodEntry
import net.coblos.moodly.domain.repository.MoodRepository
import javax.inject.Inject

class MoodRepositoryImpl @Inject constructor(
    private val moodDao: MoodDao,
    private val apiService: MoodApiService
) : MoodRepository {
    override fun getMoodHistory(): Flow<List<MoodEntry>> =
        moodDao.getAllMoods().map { list ->
            list.map { entity ->
                MoodEntry(
                    id = entity.id,
                    emotion = Emotion.values().find { it.score == entity.emotionScore } ?: Emotion.HAPPY,
                    note = entity.note,
                    photoUri = entity.photoUri,
                    createdAt = entity.createdAt,
                    isSynced = entity.isSynced
                )
            }
        }

    override suspend fun addMood(mood: MoodEntry): Result<Unit> {
        val entity = MoodEntryEntity(
            emotionScore = mood.emotion.score,
            note = mood.note,
            photoUri = mood.photoUri,
            createdAt = mood.createdAt
        )
        moodDao.insertMood(entity)
        return Result.success(Unit)
    }

    override suspend fun deleteMood(id: Long): Result<Unit> {
        moodDao.deleteMood(id)
        return Result.success(Unit)
    }

    override suspend fun syncMoods(): Result<Unit> = Result.success(Unit)
}
