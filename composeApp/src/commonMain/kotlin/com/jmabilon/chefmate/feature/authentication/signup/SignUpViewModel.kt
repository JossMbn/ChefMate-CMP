package com.jmabilon.chefmate.feature.authentication.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.feature.authentication.signup.model.SignUpAction
import com.jmabilon.chefmate.feature.authentication.signup.model.SignUpEvent
import com.jmabilon.chefmate.feature.authentication.signup.model.SignUpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class SignUpViewModel : ViewModel() {

    private val _event = MutableSharedFlow<SignUpEvent>()
    val event = _event.asSharedFlow()

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
            else -> {
                // Handle actions
            }
        }
    }

    private fun loadData() {
        // ...
    }
}
