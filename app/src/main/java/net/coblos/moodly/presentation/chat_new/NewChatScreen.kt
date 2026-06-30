package net.coblos.moodly.presentation.chat_new

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import net.coblos.moodly.domain.model.Message

@Composable
fun NewChatScreen(
    conversationId: String,
    onBackClick: () -> Unit,
    viewModel: NewChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(conversationId) {
        viewModel.loadMessages(conversationId)
    }

    Scaffold { padding ->
        if (isLoading && messages.isEmpty()) {
            // Shimmer loading
            Text("Loading...", modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                reverseLayout = true
            ) {
                items(messages.reversed()) { message ->
                    Text(text = message.content, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
