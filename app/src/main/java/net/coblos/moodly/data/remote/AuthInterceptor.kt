package net.coblos.moodly.data.remote

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import net.coblos.moodly.data.local.pref.UserPreferencesDataSource
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val preferencesDataSource: UserPreferencesDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        // Skip adding token for login request
        if (request.url.encodedPath.contains("auth/login")) {
            return chain.proceed(request)
        }

        val userPreferences = runBlocking {
            preferencesDataSource.userPreferences.firstOrNull()
        }

        val token = userPreferences?.accessToken

        val authenticatedRequest = if (!token.isNullOrBlank()) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        return chain.proceed(authenticatedRequest)
    }
}
