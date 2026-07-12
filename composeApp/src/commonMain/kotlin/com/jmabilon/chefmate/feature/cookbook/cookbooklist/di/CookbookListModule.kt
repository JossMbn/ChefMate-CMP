package com.jmabilon.chefmate.feature.cookbook.cookbooklist.di

import com.jmabilon.chefmate.feature.cookbook.cookbooklist.domain.GetCookbookListSortedCookbooksUseCase
import com.jmabilon.chefmate.feature.cookbook.cookbooklist.domain.GetCookbookListSortedCookbooksUseCaseImpl
import com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.CookbookListViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val cookbookListModule = module {

    // =================================================================================================
    // ViewModels
    // =================================================================================================

    viewModelOf(::CookbookListViewModel)

    // =================================================================================================
    // UseCases
    // =================================================================================================

    factoryOf(::GetCookbookListSortedCookbooksUseCaseImpl) { bind<GetCookbookListSortedCookbooksUseCase>() }
}
