package net.coblos.moodly.data.remote.api

import retrofit2.http.*

interface ConsultationApiService {
    @POST("consultations")
    suspend fun createConsultation(@Body request: CreateConsultationRequest): ConsultationResponse

    @GET("consultations")
    suspend fun getConsultations(): List<ConsultationResponse>

    @GET("consultations/{id}")
    suspend fun getConsultation(@Path("id") id: String): ConsultationResponse

    @PATCH("consultations/{id}")
    suspend fun closeConsultation(@Path("id") id: String, @Body request: Map<String, String> = mapOf("status" to "CLOSED")): ConsultationResponse

    @POST("consultations/{id}/messages")
    suspend fun sendMessage(@Path("id") id: String, @Body request: SendMessageRequest): MessageResponse

    @GET("consultations/{id}/messages")
    suspend fun getMessages(@Path("id") id: String, @Query("limit") limit: Int = 50): List<MessageResponse>

    @POST("consultations/{id}/read")
    suspend fun markAsRead(@Path("id") id: String, @Body request: MarkReadRequest): BasicResponse
}

data class CreateConsultationRequest(val psychologistId: String)

data class ConsultationResponse(
    val id: String,
    val psychologistId: String,
    val status: String,
    val createdAt: Long
)

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
