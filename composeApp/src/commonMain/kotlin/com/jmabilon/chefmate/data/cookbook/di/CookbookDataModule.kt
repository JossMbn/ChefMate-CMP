package com.jmabilon.chefmate.data.cookbook.di

import com.jmabilon.chefmate.data.cookbook.CookbookRepositoryImpl
import com.jmabilon.chefmate.data.cookbook.remote.CookbookRemoteDataSource
import com.jmabilon.chefmate.data.cookbook.remote.CookbookRemoteDataSourceImpl
import com.jmabilon.chefmate.domain.cookbook.repository.CookbookRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val cookbookDataModule = module {
    singleOf(::CookbookRepositoryImpl).bind<CookbookRepository>()
    singleOf(::CookbookRemoteDataSourceImpl).bind<CookbookRemoteDataSource>()
}
