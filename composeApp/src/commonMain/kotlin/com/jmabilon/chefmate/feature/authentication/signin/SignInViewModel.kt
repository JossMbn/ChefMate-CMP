package com.jmabilon.chefmate.feature.authentication.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.domain.authentication.usecase.SignInWithEmailUseCase
import com.jmabilon.chefmate.feature.authentication.signin.model.SignInAction
import com.jmabilon.chefmate.feature.authentication.signin.model.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInWithEmailUseCase: SignInWithEmailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state
        .onStart {
            // Load initial data here
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = SignInState()
        )

    fun onAction(action: SignInAction) {
        when (action) {
            is SignInAction.OnEmailValueChange -> handleNewEmailValue(action.newEmailValue)
            is SignInAction.OnPasswordValueChange -> handleNewPasswordValue(action.newPasswordValue)
            SignInAction.OnSignInClick -> signInWithEmail()
        }
    }

    private fun handleNewEmailValue(newEmailValue: String) {
        _state.update { it.copy(email = newEmailValue) }
    }

    private fun handleNewPasswordValue(newPasswordValue: String) {
        _state.update { it.copy(password = newPasswordValue) }
    }

    private fun signInWithEmail() {
        viewModelScope.launch {
            val currentState = state.value

            _state.update { it.copy(isLoading = true) }
            signInWithEmailUseCase(
                email = currentState.email,
                password = currentState.password
            )
                .onFailure { error ->
                    SnackbarController.sendError(error = error)
                }
                .also {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }
}
