package com.jmabilon.chefmate.feature.home.di

import com.jmabilon.chefmate.feature.home.domain.usecase.GetHomeScreenDataUseCase
import com.jmabilon.chefmate.feature.home.domain.usecase.GetHomeScreenDataUseCaseImpl
import com.jmabilon.chefmate.feature.home.presentation.HomeViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {

    // =================================================================================================
    // ViewModels
    // =================================================================================================

    viewModelOf(::HomeViewModel)

    // =================================================================================================
    // UseCases
    // =================================================================================================

    factoryOf(::GetHomeScreenDataUseCaseImpl) { bind<GetHomeScreenDataUseCase>() }
}
