package net.coblos.moodly.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.coblos.moodly.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(username: String,password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = userRepository.login(username, password)
            _loginState.value = if (result.isSuccess) {
                LoginState.Success(result.getOrThrow())
            } else {
                LoginState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val userPreferences: net.coblos.moodly.data.local.pref.UserPreferences) : LoginState()
    data class Error(val message: String) : LoginState()
}
