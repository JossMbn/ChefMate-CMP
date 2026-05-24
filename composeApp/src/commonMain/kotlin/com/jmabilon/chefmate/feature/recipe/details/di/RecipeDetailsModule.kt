package com.jmabilon.chefmate.feature.recipe.details.di

import com.jmabilon.chefmate.feature.recipe.details.presentation.RecipeDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val recipeDetailsModule = module {
    viewModelOf(::RecipeDetailsViewModel)
}
