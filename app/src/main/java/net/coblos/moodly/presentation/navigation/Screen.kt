package net.coblos.moodly.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddMood : Screen("add_mood")
    data object Details : Screen("details/{id}") {
        fun createRoute(id: Long) = "details/$id"
    }
}
