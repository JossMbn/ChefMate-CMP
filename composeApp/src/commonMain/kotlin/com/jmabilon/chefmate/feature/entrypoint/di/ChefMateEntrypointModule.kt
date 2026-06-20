package com.jmabilon.chefmate.feature.entrypoint.di

import com.jmabilon.chefmate.feature.entrypoint.domain.usecase.ObserveAuthenticationStatusUseCase
import com.jmabilon.chefmate.feature.entrypoint.domain.usecase.ObserveAuthenticationStatusUseCaseImpl
import com.jmabilon.chefmate.feature.entrypoint.presentation.ChefMateEntrypointViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chefMateModule = module {

    // =================================================================================================
    // ViewModels
    // =================================================================================================

    viewModelOf(::ChefMateEntrypointViewModel)

    // =================================================================================================
    // Use Cases
    // =================================================================================================

    factoryOf(::ObserveAuthenticationStatusUseCaseImpl) { bind<ObserveAuthenticationStatusUseCase>() }
}
