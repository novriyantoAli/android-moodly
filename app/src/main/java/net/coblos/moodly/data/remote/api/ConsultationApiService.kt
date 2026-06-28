package net.coblos.moodly.data.remote.api

import net.coblos.moodly.data.remote.dto.ConsultationResponse
import net.coblos.moodly.data.remote.dto.PaginatedConsultationResponse
import retrofit2.http.*

interface ConsultationApiService {
    @POST("api/v1/consultations")
    suspend fun createConsultation(@Body request: CreateConsultationRequest): ConsultationResponse

    @GET("api/v1/consultations")
    suspend fun getConsultations(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("status") status: String? = null,
        @Query("search") search: String? = null
    ): PaginatedConsultationResponse

    @GET("api/v1/consultations/{id}")
    suspend fun getConsultation(@Path("id") id: String): ConsultationResponse

    @PATCH("api/v1/consultations/{id}")
    suspend fun closeConsultation(@Path("id") id: String, @Body request: Map<String, String> = mapOf("status" to "CLOSED")): ConsultationResponse

    @POST("api/v1/consultations/{id}/messages")
    suspend fun sendMessage(@Path("id") id: String, @Body request: SendMessageRequest): MessageResponse

    @GET("api/v1/consultations/{id}/messages")
    suspend fun getMessages(@Path("id") id: String, @Query("limit") limit: Int = 50): List<MessageResponse>

    @POST("api/v1/consultations/{id}/read")
    suspend fun markAsRead(@Path("id") id: String, @Body request: MarkReadRequest): BasicResponse
}

data class CreateConsultationRequest(val psychologistId: String)

data class SendMessageRequest(val content: String)

data class MessageResponse(
    val id: String,
    val consultationId: String,
    val senderId: String,
    val content: String,
    val createdAt: Long,
    val isRead: Boolean
)

data class MarkReadRequest(val messageId: String)
