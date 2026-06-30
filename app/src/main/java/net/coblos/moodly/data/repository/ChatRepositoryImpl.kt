package net.coblos.moodly.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.coblos.moodly.data.local.db.ChatDao
import net.coblos.moodly.data.local.db.ChatMessageEntity
import net.coblos.moodly.data.remote.api.ConsultationApiService
import net.coblos.moodly.data.remote.api.SendMessageRequest
import net.coblos.moodly.domain.model.Message
import net.coblos.moodly.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val apiService: ConsultationApiService
) : ChatRepository {

    override fun getMessages(conversationId: String): Flow<List<Message>> {
        return chatDao.getMessagesByConversation(conversationId).map { entities ->
            entities.map {
                Message(it.id, it.conversationId, it.senderId, it.content, it.createdAt, it.isRead, it.status)
            }
        }
    }

    override suspend fun syncMessages(conversationId: String) {
        val remoteMessages = apiService.getMessages(conversationId)
        val entities = remoteMessages.map {
            ChatMessageEntity(it.id, it.consultationId, it.senderId, it.content, it.createdAt, it.isRead)
        }
        chatDao.insertMessages(entities)
    }

    override suspend fun sendMessage(conversationId: String, content: String) {
        // Optimistic UI update could be added here
        apiService.sendMessage(conversationId, SendMessageRequest(content))
        syncMessages(conversationId)
    }
}
