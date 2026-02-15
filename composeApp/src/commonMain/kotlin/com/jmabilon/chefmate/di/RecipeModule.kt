package com.jmabilon.chefmate.di

import com.jmabilon.chefmate.data.recipe.RecipeRepositoryImpl
import com.jmabilon.chefmate.data.recipe.source.remote.RecipeRemoteDataSource
import com.jmabilon.chefmate.data.recipe.source.remote.RecipeRemoteDataSourceImpl
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val recipeModule = module {

    // =============================================================================================
    // Data Layer
    // =============================================================================================

    singleOf(::RecipeRepositoryImpl).bind<RecipeRepository>()
    singleOf(::RecipeRemoteDataSourceImpl).bind<RecipeRemoteDataSource>()
}
