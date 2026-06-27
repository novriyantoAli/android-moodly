package net.coblos.moodly.presentation.consultation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.coblos.moodly.data.remote.api.ConsultationResponse
import net.coblos.moodly.domain.repository.ConsultationRepository
import javax.inject.Inject

@HiltViewModel
class ConsultationViewModel @Inject constructor(
    private val repository: ConsultationRepository
) : ViewModel() {
    private val _consultations = MutableStateFlow<List<ConsultationResponse>>(emptyList())
    val consultations: StateFlow<List<ConsultationResponse>> = _consultations

    init {
        loadConsultations()
    }

    private fun loadConsultations() {
        viewModelScope.launch {
            _consultations.value = repository.getConsultations()
        }
    }

    fun createConsultation(psychologistId: String) {
        viewModelScope.launch {
            repository.createConsultation(psychologistId)
            loadConsultations()
        }
    }
}
