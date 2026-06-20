package com.jmabilon.chefmate.feature.collection.details.di

import com.jmabilon.chefmate.feature.collection.details.presentation.CollectionDetailsViewModel
import com.jmabilon.chefmate.feature.collection.details.presentation.overlay.rename.RenameCollectionViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val collectionDetailsModule = module {
    viewModelOf(::CollectionDetailsViewModel)
    viewModelOf(::RenameCollectionViewModel)
}
