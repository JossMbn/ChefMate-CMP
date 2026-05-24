package com.jmabilon.chefmate.data.collection.di

import com.jmabilon.chefmate.data.collection.CollectionRepositoryImpl
import com.jmabilon.chefmate.data.collection.remote.CollectionRemoteDataSource
import com.jmabilon.chefmate.data.collection.remote.CollectionRemoteDataSourceImpl
import com.jmabilon.chefmate.domain.collection.repository.CollectionRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val collectionDataModule = module {
    singleOf(::CollectionRepositoryImpl).bind<CollectionRepository>()
    singleOf(::CollectionRemoteDataSourceImpl).bind<CollectionRemoteDataSource>()
}
