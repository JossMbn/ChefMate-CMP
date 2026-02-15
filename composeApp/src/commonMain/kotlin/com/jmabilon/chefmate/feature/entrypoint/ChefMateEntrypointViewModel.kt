package com.jmabilon.chefmate.feature.entrypoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmabilon.chefmate.domain.authentication.model.AuthenticationStatus
import com.jmabilon.chefmate.domain.authentication.usecase.ObserveAuthenticationStatusUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ChefMateEntrypointViewModel(
    observeAuthenticationStatusUseCase: ObserveAuthenticationStatusUseCase
) : ViewModel() {

    private val _authStatus = observeAuthenticationStatusUseCase()

    val authStatus: StateFlow<AuthenticationStatus> = _authStatus.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        AuthenticationStatus.Initializing
    )
}
