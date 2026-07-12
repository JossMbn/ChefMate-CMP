package com.jmabilon.chefmate.feature.overlay.cookbook.create.di

import com.jmabilon.chefmate.feature.overlay.cookbook.create.presentation.CookbookCreationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val cookbookCreationPresentationModule = module {
    viewModelOf(::CookbookCreationViewModel)
}
