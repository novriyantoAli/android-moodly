package net.coblos.moodly.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import net.coblos.moodly.data.local.pref.UserPreferencesDataSource
import net.coblos.moodly.ui.theme.MoodlyColors

@Composable
fun MainScreen(userPreferencesDataSource: UserPreferencesDataSource) {
    val navController = rememberNavController()
    val userPreferences by userPreferencesDataSource.userPreferences.collectAsState(initial = null)
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val roles = userPreferences?.roles ?: emptyList()

    val items = listOf(
        NavigationItem(
            Screen.Home.route,
            title="Home",
            Icons.Default.Home
        ),
        NavigationItem(
            Screen.Consultations.route,
            title = "Consultation",
            Icons.AutoMirrored.Filled.List,
            requiredRoles = listOf("atlit", "psikolog")
        )
    )

    val filteredItems = items.filter { item ->
        item.requiredRoles.isEmpty() || item.requiredRoles.any { roles.contains(it) }
    }

    Scaffold(
        bottomBar = {
            if (userPreferences?.accessToken != null && filteredItems.isNotEmpty()) {
                NavigationBar(
                    containerColor = MoodlyColors.SurfaceContainerLow,
                    tonalElevation = 8.dp
                ) {
                    filteredItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(text = item.title, fontSize = 12.sp) },
                            selected = currentDestination?.route == item.route,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MoodlyColors.Primary,
                                selectedTextColor = MoodlyColors.Primary,
                                indicatorColor = MoodlyColors.SecondaryContainer,
                                unselectedIconColor = MoodlyColors.OnSurfaceVariant,
                                unselectedTextColor = MoodlyColors.OnSurfaceVariant
                            ),
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
