package com.jmabilon.chefmate.di.data

import com.jmabilon.chefmate.data.authentication.AuthenticationRepositoryImpl
import com.jmabilon.chefmate.data.recipe.RecipeRepositoryImpl
import com.jmabilon.chefmate.domain.authentication.repository.AuthenticationRepository
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    // =============================================================================================
    // Authentication
    // =============================================================================================

    singleOf(::AuthenticationRepositoryImpl).bind<AuthenticationRepository>()

    // =============================================================================================
    // Recipe
    // =============================================================================================

    singleOf(::RecipeRepositoryImpl).bind<RecipeRepository>()
}
