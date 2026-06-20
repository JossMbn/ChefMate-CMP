package com.jmabilon.chefmate.feature.recipe.scanner.di

import com.jmabilon.chefmate.feature.recipe.scanner.presentation.RecipeScannerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val recipeScannerModule = module {
    viewModelOf(::RecipeScannerViewModel)
}
