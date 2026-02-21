package com.jmabilon.chefmate.domain.authentication.usecase

import com.jmabilon.chefmate.domain.authentication.repository.AuthenticationRepository

interface SignOutUseCase {
    suspend operator fun invoke(): Result<Unit>
}

class SignOutUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository
) : SignOutUseCase {

    override suspend fun invoke(): Result<Unit> {
        return authenticationRepository.signOut()
    }
}
