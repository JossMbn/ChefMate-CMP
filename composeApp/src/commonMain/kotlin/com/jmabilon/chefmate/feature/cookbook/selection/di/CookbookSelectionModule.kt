package com.jmabilon.chefmate.feature.cookbook.selection.di

import com.jmabilon.chefmate.feature.cookbook.selection.presentation.CookbookSelectionViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val cookbookSelectionModule = module {
    viewModelOf(::CookbookSelectionViewModel)
}
