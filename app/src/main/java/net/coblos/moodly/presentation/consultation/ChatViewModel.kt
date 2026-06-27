package net.coblos.moodly.presentation.consultation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.coblos.moodly.data.remote.api.MessageResponse
import net.coblos.moodly.domain.repository.ConsultationRepository
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ConsultationRepository
) : ViewModel() {
    private val _messages = MutableStateFlow<List<MessageResponse>>(emptyList())
    val messages: StateFlow<List<MessageResponse>> = _messages

    fun startPolling(consultationId: String) {
        viewModelScope.launch {
            while (true) {
                _messages.value = repository.getMessages(consultationId)
                delay(5000)
            }
        }
    }

    fun sendMessage(consultationId: String, content: String) {
        viewModelScope.launch {
            repository.sendMessage(consultationId, content)
            _messages.value = repository.getMessages(consultationId)
        }
    }
}
