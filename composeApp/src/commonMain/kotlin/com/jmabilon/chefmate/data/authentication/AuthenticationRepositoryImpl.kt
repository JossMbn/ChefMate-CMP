package com.jmabilon.chefmate.data.authentication

import com.jmabilon.chefmate.core.data.cache.DataCache
import com.jmabilon.chefmate.data.authentication.remote.AuthenticationRemoteDataSource
import com.jmabilon.chefmate.domain.authentication.repository.AuthenticationRepository
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val cache: DataCache
) : AuthenticationRepository {

    override val authStatus: Flow<SessionStatus>
        get() = authenticationRemoteDataSource.authStatus

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<Unit> {
        return authenticationRemoteDataSource.signInWithEmail(
            email = email,
            password = password
        )
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): Result<Unit> {
        return authenticationRemoteDataSource.signUpWithEmail(
            email = email,
            password = password
        )
    }

    override suspend fun signOut(): Result<Unit> {
        return authenticationRemoteDataSource.signOut()
            .onSuccess {
                cache.clear(false)
            }
    }
}
