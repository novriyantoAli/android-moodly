package net.coblos.moodly.domain.model

data class Message(
    val id: String,
    val consultationId: String,
    val senderId: String,
    val content: String,
    val createdAt: Long,
    val isRead: Boolean,
    val status: String
)
