package com.jmabilon.chefmate.domain.recipe.usecase

import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

interface ObserveRecipeById {
        operator fun invoke(recipeId: String): Flow<RecipeDomain>
}

class ObserveRecipeByIdImpl(
    private val recipeRepository: RecipeRepository
) : ObserveRecipeById {

    override fun invoke(recipeId: String): Flow<RecipeDomain> {
        return recipeRepository.observeRecipeById(recipeId = recipeId)
    }
}
