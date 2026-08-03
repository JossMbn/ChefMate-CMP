package com.jmabilon.chefmate.feature.recipe.creation2.di

import com.jmabilon.chefmate.feature.recipe.creation2.presentation.RecipeEditorViewModel
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.stateholder.RecipeEditorStateHolder
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val recipeEditorModule = module {

    viewModelOf(::RecipeEditorViewModel)

    factoryOf(::RecipeEditorStateHolder)
}
