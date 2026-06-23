package net.coblos.moodly.data.local.pref

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val USERNAME_KEY = stringPreferencesKey("username")
    private val DARK_THEME_KEY = booleanPreferencesKey("is_dark_theme")

    val userPreferences = context.dataStore.data.map { prefs ->
        Pair(prefs[USERNAME_KEY] ?: "", prefs[DARK_THEME_KEY] ?: false)
    }

    suspend fun updateUsername(username: String) {
        context.dataStore.edit { prefs -> prefs[USERNAME_KEY] = username }
    }

    suspend fun updateDarkTheme(isDark: Boolean) {
        context.dataStore.edit { prefs -> prefs[DARK_THEME_KEY] = isDark }
    }
}
