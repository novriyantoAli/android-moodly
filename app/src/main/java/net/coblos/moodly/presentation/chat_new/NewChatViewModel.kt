package net.coblos.moodly.presentation.chat_new

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.coblos.moodly.domain.model.Message
import net.coblos.moodly.domain.repository.ChatRepository
import javax.inject.Inject

@HiltViewModel
class NewChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            chatRepository.getMessages(conversationId).collect {
                _messages.value = it
                _isLoading.value = false
            }
        }
        viewModelScope.launch {
            chatRepository.syncMessages(conversationId)
        }
    }

    fun sendMessage(conversationId: String, content: String) {
        viewModelScope.launch {
            chatRepository.sendMessage(conversationId, content)
        }
    }
}
