package net.coblos.moodly.domain.repository

import kotlinx.coroutines.flow.Flow
import net.coblos.moodly.data.local.pref.UserPreferences

interface UserRepository {
    val userPreferences: Flow<UserPreferences>
    suspend fun login(username: String, password: String): Result<UserPreferences>
    suspend fun logout(): Result<Unit>
}
