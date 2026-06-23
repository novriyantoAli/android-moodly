package net.coblos.moodly.domain.model

data class MoodEntry(
    val id: Long = 0,
    val emotion: Emotion,
    val note: String,
    val photoUri: String?,
    val createdAt: Long,
    val isSynced: Boolean = false
)
