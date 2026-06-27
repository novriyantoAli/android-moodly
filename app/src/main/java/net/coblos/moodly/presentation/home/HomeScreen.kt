package net.coblos.moodly.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import net.coblos.moodly.data.local.pref.UserPreferences
import net.coblos.moodly.presentation.navigation.Screen
import net.coblos.moodly.domain.model.MoodEntry

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val moodList by viewModel.moodHistory.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Mood History")
                Button(onClick = { viewModel.logout() }) { // Assuming logout function exists in ViewModel
                    Text("Logout")
                }
            }
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(moodList) {
                MoodItem(mood = it) // Assuming MoodItem composable exists
            }
        }
    }
}

@Composable
fun MoodItem(mood: MoodEntry) {
    Column(modifier = Modifier.padding(8.dp))
    {
        Text(text = "${mood.emotion.label}: ${mood.note}")
    }
}
