package net.coblos.moodly.data.repository

import net.coblos.moodly.data.remote.api.ConsultationApiService
import net.coblos.moodly.data.remote.api.ConsultationResponse
import net.coblos.moodly.data.remote.api.CreateConsultationRequest
import net.coblos.moodly.data.remote.api.MessageResponse
import net.coblos.moodly.data.remote.api.SendMessageRequest
import net.coblos.moodly.domain.repository.ConsultationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsultationRepositoryImpl @Inject constructor(
    private val apiService: ConsultationApiService
) : ConsultationRepository {
    override suspend fun createConsultation(psychologistId: String) = apiService.createConsultation(CreateConsultationRequest(psychologistId))
    override suspend fun getConsultations() = apiService.getConsultations()
    override suspend fun getConsultation(id: String) = apiService.getConsultation(id)
    override suspend fun closeConsultation(id: String) = apiService.closeConsultation(id)
    override suspend fun sendMessage(id: String, content: String) = apiService.sendMessage(id, SendMessageRequest(content))
    override suspend fun getMessages(id: String) = apiService.getMessages(id)
    override suspend fun markAsRead(id: String, messageId: String) = apiService.markAsRead(id, net.coblos.moodly.data.remote.api.MarkReadRequest(messageId))
}
