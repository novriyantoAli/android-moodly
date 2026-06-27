package net.coblos.moodly.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.coblos.moodly.data.local.pref.UserPreferences
import net.coblos.moodly.data.local.pref.UserPreferencesDataSource
import net.coblos.moodly.presentation.home.HomeScreen
import net.coblos.moodly.presentation.login.LoginScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    userPreferencesDataSource: UserPreferencesDataSource
) {
    val userPreferencesState by userPreferencesDataSource.userPreferences.collectAsState(initial = UserPreferences("", false, null, null, null, emptyList(), emptyList()))

    val startDestination = remember(userPreferencesState.accessToken) {
        if (!userPreferencesState.accessToken.isNullOrEmpty()) {
            Screen.Home.route
        } else {
            Screen.Login.route
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        // Additional screens to be implemented
    }
}
