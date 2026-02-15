package com.jmabilon.chefmate.di.data

import com.jmabilon.chefmate.data.authentication.source.remote.AuthenticationRemoteDataSource
import com.jmabilon.chefmate.data.authentication.source.remote.AuthenticationRemoteDataSourceImpl
import com.jmabilon.chefmate.data.recipe.source.remote.RecipeRemoteDataSource
import com.jmabilon.chefmate.data.recipe.source.remote.RecipeRemoteDataSourceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataSourceModule = module {

    // =============================================================================================
    // Authentication
    // =============================================================================================

    singleOf(::AuthenticationRemoteDataSourceImpl).bind<AuthenticationRemoteDataSource>()

    // =============================================================================================
    // Recipe
    // =============================================================================================

    singleOf(::RecipeRemoteDataSourceImpl).bind<RecipeRemoteDataSource>()
}
