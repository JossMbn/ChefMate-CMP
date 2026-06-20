package com.jmabilon.chefmate.feature.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.domain.authentication.repository.AuthenticationRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountViewModel(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<AccountEvent>()
    val event = _event.asSharedFlow()

    private val _state = MutableStateFlow(AccountState())
    val state = _state
        .onStart {
            // Load initial data here
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = AccountState()
        )

    fun onAction(action: AccountAction) {
        when (action) {
            AccountAction.OnSignOutClick -> signOut()
        }
    }

    private fun loadData() {
        // ...
    }

    private fun signOut() {
        viewModelScope.launch {
            authenticationRepository.signOut()
                .onFailure { error ->
                    SnackbarController.sendError(error = error)
                }
        }
    }
}
