package net.coblos.moodly.data.repository

import net.coblos.moodly.data.remote.api.ConsultationApiService
import net.coblos.moodly.data.remote.api.CreateConsultationRequest
import net.coblos.moodly.data.remote.api.MarkReadRequest
import net.coblos.moodly.data.remote.api.SendMessageRequest
import net.coblos.moodly.data.remote.dto.ConsultationResponse
import net.coblos.moodly.data.remote.dto.PaginatedConsultationResponse
import net.coblos.moodly.domain.repository.ConsultationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsultationRepositoryImpl @Inject constructor(
    private val apiService: ConsultationApiService
) : ConsultationRepository {
    override suspend fun createConsultation(psychologistId: String): ConsultationResponse = apiService.createConsultation(CreateConsultationRequest(psychologistId))
    override suspend fun getConsultations(page: Int, limit: Int, status: String?, search: String?): PaginatedConsultationResponse = apiService.getConsultations(page, limit, status, search)
    override suspend fun getConsultation(id: String): ConsultationResponse = apiService.getConsultation(id)
    override suspend fun closeConsultation(id: String): ConsultationResponse = apiService.closeConsultation(id)
    override suspend fun sendMessage(id: String, content: String) = apiService.sendMessage(id, SendMessageRequest(content))
    override suspend fun getMessages(id: String) = apiService.getMessages(id)
    override suspend fun markAsRead(id: String, messageId: String) = apiService.markAsRead(id, MarkReadRequest(messageId))
}
