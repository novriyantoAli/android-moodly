package net.coblos.moodly.data.remote.api

data class BasicResponse(
    val success: Boolean,
    val message: String,
    val error: Any? = null
)
