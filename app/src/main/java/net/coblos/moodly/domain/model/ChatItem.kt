package net.coblos.moodly.domain.model

data class ChatItem(
    val id: String,
    val name: String,
    val avatarEmoji: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val isUnread: Boolean = false,
    val isVoiceNote: Boolean = false,
    val isSent: Boolean = false
)