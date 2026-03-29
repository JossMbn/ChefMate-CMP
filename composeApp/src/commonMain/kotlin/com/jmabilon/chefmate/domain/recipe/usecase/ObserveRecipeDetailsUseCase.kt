package com.jmabilon.chefmate.domain.recipe.usecase

import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

interface ObserveRecipeDetailsUseCase {
    operator fun invoke(recipeId: String): Flow<RecipeDomain>
}

class ObserveRecipeDetailsUseCaseImpl(
    private val recipeRepository: RecipeRepository
) : ObserveRecipeDetailsUseCase {

    override fun invoke(recipeId: String): Flow<RecipeDomain> =
        recipeRepository.observeRecipeById(recipeId = recipeId)
}
