package com.jmabilon.chefmate.feature.entrypoint.domain.usecase

import com.jmabilon.chefmate.domain.authentication.repository.AuthenticationRepository
import com.jmabilon.chefmate.feature.entrypoint.domain.model.AuthenticationStatus
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ObserveAuthenticationStatusUseCase {
    operator fun invoke(): Flow<AuthenticationStatus>
}

class ObserveAuthenticationStatusUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository
) : ObserveAuthenticationStatusUseCase {

    override operator fun invoke(): Flow<AuthenticationStatus> {
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
