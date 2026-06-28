package net.coblos.moodly.data.remote.dto


data class PaginatedConsultationResponse(
    val data: List<ConsultationResponse>,
    val meta: MetaResponse
)

data class MetaResponse(
    val currentPage: Int,
    val totalPages: Int
)

data class ConsultationResponse(
    val conversation_id: String,
    val psychologist_id: Long,
    val status: String,
    val started_at: String?,
    val closed_at: String?,
    val created_at: String,
    val participant: ParticipantResponse,
    val psychologist: PsychologistResponse,
)

data class ParticipantResponse(
    val name: String
)

data class PsychologistResponse(
    val name: String
)
