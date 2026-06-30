package net.coblos.moodly.domain.repository

import kotlinx.coroutines.flow.Flow
import net.coblos.moodly.domain.model.Message

interface ChatRepository {
    fun getMessages(conversationId: String): Flow<List<Message>>
    suspend fun syncMessages(conversationId: String)
    suspend fun sendMessage(conversationId: String, content: String)
}
