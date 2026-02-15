package com.jmabilon.chefmate.domain.authentication.usecase

import com.jmabilon.chefmate.domain.authentication.repository.AuthenticationRepository

interface SignUpWithEmailUseCase {
    suspend operator fun invoke(email: String, password: String): Result<Unit>
}

class SignUpWithEmailUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository
) : SignUpWithEmailUseCase {

    override suspend fun invoke(email: String, password: String): Result<Unit> {
        return authenticationRepository.signUpWithEmail(email = email, password = password)
    }
}
