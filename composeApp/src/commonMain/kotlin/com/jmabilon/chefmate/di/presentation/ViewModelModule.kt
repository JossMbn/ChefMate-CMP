package com.jmabilon.chefmate.di.presentation

import com.jmabilon.chefmate.feature.account.AccountViewModel
import com.jmabilon.chefmate.feature.authentication.signin.SignInViewModel
import com.jmabilon.chefmate.feature.authentication.signup.SignUpViewModel
import com.jmabilon.chefmate.feature.collection.details.CollectionDetailsViewModel
import com.jmabilon.chefmate.feature.collection.selection.CollectionSelectionViewModel
import com.jmabilon.chefmate.feature.entrypoint.ChefMateEntrypointViewModel
import com.jmabilon.chefmate.feature.home.HomeViewModel
import com.jmabilon.chefmate.feature.recipe.creation.ManualRecipeCreationViewModel
import com.jmabilon.chefmate.feature.recipe.details.RecipeDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    // =============================================================================================
    // Application
    // =============================================================================================

    viewModelOf(::ChefMateEntrypointViewModel)

    // =============================================================================================
    // Authentication
    // =============================================================================================

    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)

    // =============================================================================================
    // Account
    // =============================================================================================

    viewModelOf(::AccountViewModel)

    // =============================================================================================
    // Home
    // =============================================================================================

    viewModelOf(::HomeViewModel)

    // =============================================================================================
    // Recipe
    // =============================================================================================

    viewModelOf(::ManualRecipeCreationViewModel)
    viewModelOf(::RecipeDetailsViewModel)

    // =============================================================================================
    // Collection
    // =============================================================================================

    viewModelOf(::CollectionDetailsViewModel)
    viewModelOf(::CollectionSelectionViewModel)
}
