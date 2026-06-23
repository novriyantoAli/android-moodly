package net.coblos.moodly.presentation.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.coblos.moodly.domain.repository.MoodRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MoodRepository
) : ViewModel() {
    val moodHistory = repository.getMoodHistory()
}
