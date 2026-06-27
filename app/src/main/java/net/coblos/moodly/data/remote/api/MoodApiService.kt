package net.coblos.moodly.data.remote.api

import retrofit2.http.*

interface MoodApiService {
    @GET("moods")
    suspend fun getMoods(): List<MoodEntryDto>

    @POST("moods")
    suspend fun addMood(@Body mood: MoodEntryDto): MoodEntryDto

    @DELETE("moods/{id}")
    suspend fun deleteMood(@Path("id") id: Long)

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/v1/logout")
    suspend fun logout(@Body request: LogoutRequest): BasicResponse
}

data class MoodEntryDto(
    val id: Long? = null,
    val emotionScore: Int,
    val note: String,
    val photoUri: String?,
    val createdAt: Long
)
