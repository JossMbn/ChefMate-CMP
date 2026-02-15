package com.jmabilon.chefmate.domain.authentication.usecase

import com.jmabilon.chefmate.domain.authentication.repository.AuthenticationRepository

interface SignInWithEmailUseCase {
    suspend operator fun invoke(email: String, password: String): Result<Unit>
}

class SignInWithEmailUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository
) : SignInWithEmailUseCase {

    override suspend fun invoke(email: String, password: String): Result<Unit> {
        return authenticationRepository.signInWithEmail(email = email, password = password)
    }
}
