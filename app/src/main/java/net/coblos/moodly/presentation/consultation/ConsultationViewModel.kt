package net.coblos.moodly.presentation.consultation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.combine
import net.coblos.moodly.data.remote.dto.ConsultationResponse
import net.coblos.moodly.domain.model.ChatItem
import net.coblos.moodly.domain.repository.ConsultationRepository
import javax.inject.Inject

@HiltViewModel
class ConsultationViewModel @Inject constructor(
    private val repository: ConsultationRepository
) : ViewModel() {
    private val _consultations = MutableStateFlow<List<ConsultationResponse>>(emptyList())
    val consultations: StateFlow<List<ConsultationResponse>> = _consultations
    private var currentPage = 1
    private var isLastPage = false

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedStatus = MutableStateFlow<String?>(null)
    val selectedStatus: StateFlow<String?> = _selectedStatus.asStateFlow()

    private val _uiState = MutableStateFlow<ConsultationUiState>(ConsultationUiState.Loading)
    val uiState: StateFlow<ConsultationUiState> = _uiState.asStateFlow()

    init {
        @OptIn(FlowPreview::class)
        viewModelScope.launch {
            combine(_searchQuery.debounce(500), _selectedStatus) { query, status ->
                query to status
            }.collectLatest { (query, status) ->
                currentPage = 1
                isLastPage = false
                fetchData(query, status, isReset = true)
            }
        }
    }

    private suspend fun fetchData(query: String?, status: String?, isReset: Boolean) {
        _uiState.value = ConsultationUiState.Loading
        try {
            val response = repository.getConsultations(currentPage, 10, status, query)
            isLastPage = currentPage >= response.meta.totalPages
            val data = response.data
            _uiState.value = ConsultationUiState.Success(data, isReset)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiState.value = ConsultationUiState.Error(e.message ?: "Unknown Error")
        }
    }

    fun loadMore() {
        if (!isLastPage) {
            currentPage++
            viewModelScope.launch {
                fetchData(_searchQuery.value, _selectedStatus.value, isReset = false)
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setStatus(status: String?) {
        _selectedStatus.value = status
    }
}

sealed interface ConsultationUiState {
    object Loading : ConsultationUiState
    data class Success(val data: List<ConsultationResponse>, val isReset: Boolean) : ConsultationUiState
    data class Error(val message: String) : ConsultationUiState
}
