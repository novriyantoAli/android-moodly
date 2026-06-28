package net.coblos.moodly.domain.repository

import net.coblos.moodly.data.remote.dto.ConsultationResponse
import net.coblos.moodly.data.remote.dto.PaginatedConsultationResponse
import net.coblos.moodly.data.remote.api.MessageResponse

interface ConsultationRepository {
    suspend fun createConsultation(psychologistId: String): ConsultationResponse
    suspend fun getConsultations(page: Int, limit: Int, status: String?, search: String?): PaginatedConsultationResponse
    suspend fun getConsultation(id: String): ConsultationResponse
    suspend fun closeConsultation(id: String): ConsultationResponse
    suspend fun sendMessage(id: String, content: String): MessageResponse
    suspend fun getMessages(id: String): List<MessageResponse>
    suspend fun markAsRead(id: String, messageId: String): Any
}
