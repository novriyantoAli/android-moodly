package net.coblos.moodly.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import net.coblos.moodly.data.local.pref.UserPreferences
import net.coblos.moodly.data.local.pref.UserPreferencesDataSource
import net.coblos.moodly.presentation.consultation.ChatScreen
import net.coblos.moodly.presentation.consultation.ConsultationListScreen
import net.coblos.moodly.presentation.home.HomeScreen
import net.coblos.moodly.presentation.login.LoginScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun NavGraph(
    navController: NavHostController,
    userPreferencesDataSource: UserPreferencesDataSource,
    modifier: Modifier = Modifier
) {
    val userPreferencesState by userPreferencesDataSource.userPreferences.collectAsState(initial = UserPreferences("", false, null, null, null, emptyList(), emptyList()))

    val startDestination = remember(userPreferencesState.accessToken) {
        if (!userPreferencesState.accessToken.isNullOrEmpty()) {
            Screen.Home.route
        } else {
            Screen.Login.route
        }
    }

    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Consultations.route) {
            ConsultationListScreen(onConsultationClick = { consultationId ->
                navController.navigate(Screen.Chat.createRoute(consultationId))
            })
        }
        composable(
            route = Screen.Chat.route,
            arguments = listOf(navArgument("consultationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val consultationId = backStackEntry.arguments?.getString("consultationId") ?: ""
            ChatScreen(consultationId = consultationId)
        }
    }
}
