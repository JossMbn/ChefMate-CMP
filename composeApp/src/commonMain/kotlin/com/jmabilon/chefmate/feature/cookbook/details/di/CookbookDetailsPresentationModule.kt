package com.jmabilon.chefmate.feature.cookbook.details.di

import com.jmabilon.chefmate.feature.cookbook.details.presentation.CookbookDetailsViewModel
import com.jmabilon.chefmate.feature.cookbook.details.presentation.overlay.rename.RenameCookbookViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val cookbookDetailsModule = module {

    viewModelOf(::CookbookDetailsViewModel)

    viewModelOf(::RenameCookbookViewModel)
}
