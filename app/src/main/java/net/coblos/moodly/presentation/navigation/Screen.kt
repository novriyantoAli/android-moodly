package net.coblos.moodly.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddMood : Screen("add_mood")
    data object Details : Screen("details/{id}") {
        fun createRoute(id: Long) = "details/$id"
    }
    data object Login : Screen("login")
    data object Consultations : Screen("consultations")
    data object Chat : Screen("chat/{consultationId}") {
        fun createRoute(consultationId: String) = "chat/$consultationId"
    }
}
