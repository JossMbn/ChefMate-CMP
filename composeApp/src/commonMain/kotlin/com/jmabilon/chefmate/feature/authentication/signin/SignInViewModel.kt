package com.jmabilon.chefmate.feature.authentication.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.feature.authentication.signin.model.SignInAction
import com.jmabilon.chefmate.feature.authentication.signin.model.SignInEvent
import com.jmabilon.chefmate.feature.authentication.signin.model.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class SignInViewModel : ViewModel() {

    private val _event = MutableSharedFlow<SignInEvent>()
    val event = _event.asSharedFlow()

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
            else -> {
                // Handle actions
            }
        }
    }

    private fun loadData() {
        // ...
    }
}
