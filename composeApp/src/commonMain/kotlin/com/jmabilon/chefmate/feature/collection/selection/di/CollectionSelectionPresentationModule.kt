package com.jmabilon.chefmate.feature.collection.selection.di

import com.jmabilon.chefmate.feature.collection.selection.presentation.CollectionSelectionViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val collectionSelectionModule = module {
    viewModelOf(::CollectionSelectionViewModel)
}
