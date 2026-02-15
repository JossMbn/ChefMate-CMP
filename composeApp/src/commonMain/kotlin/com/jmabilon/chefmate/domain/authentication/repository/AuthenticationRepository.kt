package com.jmabilon.chefmate.domain.authentication.repository

import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    val authStatus: Flow<SessionStatus>

    suspend fun signInWithEmail(email: String, password: String): Result<Unit>
    suspend fun signUpWithEmail(email: String, password: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
}
