package com.jmabilon.chefmate.domain.authentication.usecase

import com.jmabilon.chefmate.domain.authentication.model.AuthenticationStatus
import com.jmabilon.chefmate.domain.authentication.repository.AuthenticationRepository
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveAuthenticationStatusUseCase(
    private val authenticationRepository: AuthenticationRepository
) {

    operator fun invoke(): Flow<AuthenticationStatus> {
        return authenticationRepository.authStatus
            .map { authStatus ->
                when (authStatus) {
                    is SessionStatus.Authenticated -> AuthenticationStatus.Authenticated
                    SessionStatus.Initializing -> AuthenticationStatus.Initializing
                    is SessionStatus.NotAuthenticated -> AuthenticationStatus.NotAuthenticated
                    is SessionStatus.RefreshFailure -> AuthenticationStatus.RefreshFailure
                }
            }
    }
}
