package net.coblos.moodly.data.remote.api

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val access_token: String,
    val refresh_token: String,
    val expired_at: Long,
    val user_id: UInt,
    val roles: List<String>,
    val permissions: List<String>
)
