package com.jmabilon.chefmate.data.recipe.di

import com.jmabilon.chefmate.data.recipe.RecipeRepositoryImpl
import com.jmabilon.chefmate.data.recipe.remote.RecipeRemoteDataSource
import com.jmabilon.chefmate.data.recipe.remote.RecipeRemoteDataSourceImpl
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val recipeDataModule = module {
    singleOf(::RecipeRepositoryImpl).bind<RecipeRepository>()
    singleOf(::RecipeRemoteDataSourceImpl).bind<RecipeRemoteDataSource>()
}
