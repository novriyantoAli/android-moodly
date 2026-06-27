package net.coblos.moodly.data.local.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.coblos.moodly.data.remote.api.LoginData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    private val USERNAME_KEY = stringPreferencesKey("username")
    private val DARK_THEME_KEY = booleanPreferencesKey("is_dark_theme")
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val ROLES_KEY = stringPreferencesKey("roles")
    private val PERMISSIONS_KEY = stringPreferencesKey("permissions")

    val userPreferences: Flow<UserPreferences> = context.dataStore.data.map {
        UserPreferences(
            username = it[USERNAME_KEY] ?: "",
            isDarkTheme = it[DARK_THEME_KEY] ?: false,
            accessToken = it[ACCESS_TOKEN_KEY],
            refreshToken = it[REFRESH_TOKEN_KEY],
            userId = it[USER_ID_KEY]?.toUIntOrNull(),
            roles = it[ROLES_KEY]?.let { json -> gson.fromJson<List<String>>(json, object : TypeToken<List<String>>() {}.type) } ?: emptyList(),
            permissions = it[PERMISSIONS_KEY]?.let { json -> gson.fromJson<List<String>>(json, object : TypeToken<List<String>>() {}.type) } ?: emptyList()
        )
    }

    suspend fun saveTokens(loginData: LoginData) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = loginData.access_token
            prefs[REFRESH_TOKEN_KEY] = loginData.refresh_token
            prefs[USER_ID_KEY] = loginData.user_id.toString()
            prefs[ROLES_KEY] = gson.toJson(loginData.roles)
            prefs[PERMISSIONS_KEY] = gson.toJson(loginData.permissions)
        }
    }

    suspend fun clearTokens() {
        context.dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
            prefs.remove(USER_ID_KEY)
            prefs.remove(ROLES_KEY)
            prefs.remove(PERMISSIONS_KEY)
        }
    }

    suspend fun updateUsername(username: String) {
        context.dataStore.edit { prefs -> prefs[USERNAME_KEY] = username }
    }

    suspend fun updateDarkTheme(isDark: Boolean) {
        context.dataStore.edit { prefs -> prefs[DARK_THEME_KEY] = isDark }
    }
}

// New data class to hold all user preferences
data class UserPreferences(
    val username: String,
    val isDarkTheme: Boolean,
    val accessToken: String?,
    val refreshToken: String?,
    val userId: UInt?,
    val roles: List<String>,
    val permissions: List<String>
)
