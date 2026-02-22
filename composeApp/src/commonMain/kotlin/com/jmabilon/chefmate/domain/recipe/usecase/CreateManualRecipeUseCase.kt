package com.jmabilon.chefmate.domain.recipe.usecase

import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository

interface CreateManualRecipeUseCase {
    suspend operator fun invoke(recipe: RecipeDomain): Result<RecipeDomain>
}

class CreateManualRecipeUseCaseImpl(
    private val recipeRepository: RecipeRepository
) : CreateManualRecipeUseCase {

    override suspend operator fun invoke(recipe: RecipeDomain): Result<RecipeDomain> {
        return recipeRepository.createRecipe(
            recipe = recipe,
            collectionIds = emptyList()
        )
    }
}
