package com.jmabilon.chefmate.feature.authentication.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.domain.authentication.usecase.SignUpWithEmailUseCase
import com.jmabilon.chefmate.feature.authentication.signup.model.SignUpAction
import com.jmabilon.chefmate.feature.authentication.signup.model.SignUpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state
        .onStart {
            // Load initial data here
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = SignUpState()
        )

    fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.OnEmailValueChange -> handleNewEmailValue(action.newEmailValue)
            is SignUpAction.OnPasswordValueChange -> handleNewPasswordValue(action.newPasswordValue)
            SignUpAction.OnSignUpClick -> handleSignUpClick()
        }
    }

    private fun handleNewEmailValue(newEmailValue: String) {
        _state.update { it.copy(email = newEmailValue) }
    }

    private fun handleNewPasswordValue(newPasswordValue: String) {
        _state.update { it.copy(password = newPasswordValue) }
    }

    private fun handleSignUpClick() {
        // TODO : Validate email and password here

        signUpWithEmail()
    }

    private fun signUpWithEmail() {
        viewModelScope.launch {
            val currentState = state.value

            _state.update { it.copy(isLoading = true) }
            signUpWithEmailUseCase(
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
