package net.coblos.moodly.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderId: String,
    val content: String,
    val createdAt: Long,
    val isRead: Boolean,
    val status: String = "SENT" // SENT, PENDING, ERROR
)
