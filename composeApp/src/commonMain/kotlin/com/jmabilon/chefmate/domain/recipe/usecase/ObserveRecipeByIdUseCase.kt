package com.jmabilon.chefmate.domain.recipe.usecase

import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

interface ObserveRecipeByIdUseCase {
    operator fun invoke(recipeId: String): Flow<RecipeDomain>
}

class ObserveRecipeByIdUseCaseImpl(
    private val recipeRepository: RecipeRepository
) : ObserveRecipeByIdUseCase {

    override fun invoke(recipeId: String): Flow<RecipeDomain> {
        return recipeRepository.observeRecipeById(recipeId = recipeId)
    }
}
