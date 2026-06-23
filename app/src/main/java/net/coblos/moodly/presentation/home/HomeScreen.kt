package net.coblos.moodly.presentation.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val moodList by viewModel.moodHistory.collectAsState(initial = emptyList())

    LazyColumn {
        items(moodList) { mood ->
            Text(text = "${mood.emotion.label}: ${mood.note}")
        }
    }
}
