package com.jmabilon.chefmate.di.domain

import com.jmabilon.chefmate.domain.authentication.usecase.ObserveAuthenticationStatusUseCase
import com.jmabilon.chefmate.domain.authentication.usecase.SignInWithEmailUseCase
import com.jmabilon.chefmate.domain.authentication.usecase.SignInWithEmailUseCaseImpl
import com.jmabilon.chefmate.domain.authentication.usecase.SignOutUseCase
import com.jmabilon.chefmate.domain.authentication.usecase.SignOutUseCaseImpl
import com.jmabilon.chefmate.domain.authentication.usecase.SignUpWithEmailUseCase
import com.jmabilon.chefmate.domain.authentication.usecase.SignUpWithEmailUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val useCaseModule = module {

    // =============================================================================================
    // Authentication
    // =============================================================================================

    factoryOf(::ObserveAuthenticationStatusUseCase)
    factoryOf(::SignInWithEmailUseCaseImpl).bind<SignInWithEmailUseCase>()
    factoryOf(::SignUpWithEmailUseCaseImpl).bind<SignUpWithEmailUseCase>()
    factoryOf(::SignOutUseCaseImpl).bind<SignOutUseCase>()
}
