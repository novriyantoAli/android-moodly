package net.coblos.moodly.data.repository

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import net.coblos.moodly.data.local.pref.UserPreferences
import net.coblos.moodly.data.local.pref.UserPreferencesDataSource
import net.coblos.moodly.data.remote.api.LoginData
import net.coblos.moodly.data.remote.api.LoginRequest
import net.coblos.moodly.data.remote.api.LoginResponse
import net.coblos.moodly.data.remote.api.LogoutRequest
import net.coblos.moodly.data.remote.api.MoodApiService
import net.coblos.moodly.domain.repository.UserRepository
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val preferencesDataSource: UserPreferencesDataSource,
    private val apiService: MoodApiService,
    private val gson: Gson
) : UserRepository {

    override val userPreferences: Flow<UserPreferences> = preferencesDataSource.userPreferences

    override suspend fun login(username: String, password: String): Result<UserPreferences> {
        return try {
            val request = LoginRequest(username, password)
            val response = apiService.login(request)
            if (response.success && response.data != null) {
                preferencesDataSource.saveTokens(response.data)
                Result.success(response.data.toUserPreferences())
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = try {
                gson.fromJson(errorBody, LoginResponse::class.java)
            } catch (ex: Exception) {
                null
            }
            val message = errorResponse?.message ?: "Login failed"
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Log.e("UserRepository", "Login failed", e)
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            val currentUserPreferences = preferencesDataSource.userPreferences.firstOrNull()
            val refreshToken = currentUserPreferences?.refreshToken

            if (!refreshToken.isNullOrBlank()) {
                apiService.logout(LogoutRequest(refreshToken))
            }
            preferencesDataSource.clearTokens()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("UserRepository", "Logout failed", e)
            Result.failure(e)
        }
    }
}

fun LoginData.toUserPreferences(): UserPreferences {
    return UserPreferences(
        username = "",
        isDarkTheme = false,
        accessToken = access_token,
        refreshToken = refresh_token,
        userId = user_id,
        roles = roles,
        permissions = permissions
    )
}
