package net.coblos.moodly.presentation.consultation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChatScreen(
    consultationId: String,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    var text by remember { mutableStateOf("") }

    LaunchedEffect(consultationId) {
        viewModel.startPolling(consultationId)
    }

    Column {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                Text(text = "${message.senderId}: ${message.content}")
            }
        }
        Row {
            OutlinedTextField(value = text, onValueChange = { text = it })
            Button(onClick = {
                viewModel.sendMessage(consultationId, text)
                text = ""
            }) {
                Text("Send")
            }
        }
    }
}
