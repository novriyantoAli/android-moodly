package net.coblos.moodly.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import net.coblos.moodly.data.local.pref.UserPreferencesDataSource

@Composable
fun MainScreen(userPreferencesDataSource: UserPreferencesDataSource) {
    val navController = rememberNavController()
    val userPreferences by userPreferencesDataSource.userPreferences.collectAsState(initial = null)
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val roles = userPreferences?.roles ?: emptyList()

    val items = listOf(
        NavigationItem(Screen.Home.route, "Home", Icons.Default.Home),
        NavigationItem(Screen.Consultations.route, "Konsultasi", Icons.AutoMirrored.Filled.List, listOf("Patient", "Counselor"))
    )

    val filteredItems = items.filter { item ->
        item.requiredRoles.isEmpty() || item.requiredRoles.any { roles.contains(it) }
    }

    Scaffold(
        bottomBar = {
            if (userPreferences?.accessToken != null && filteredItems.isNotEmpty()) {
                NavigationBar {
                    filteredItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.title) },
                            selected = currentDestination?.route == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            userPreferencesDataSource = userPreferencesDataSource,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
