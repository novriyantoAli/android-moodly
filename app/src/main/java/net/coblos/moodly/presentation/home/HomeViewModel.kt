package net.coblos.moodly.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.coblos.moodly.domain.repository.UserRepository
import net.coblos.moodly.domain.model.MoodEntry
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moodRepository: net.coblos.moodly.domain.repository.MoodRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val moodHistory: StateFlow<List<MoodEntry>> = moodRepository.getMoodHistory()
        .mapNotNull { it } // Ensure we only pass non-null lists
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            // Navigation to Login screen will be handled by the UI based on the state change
        }
    }
}
