package com.jmabilon.chefmate.domain.recipe.usecase

import com.jmabilon.chefmate.domain.collection.repository.CollectionRepository
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository

interface UpdateRecipeUseCase {
    suspend operator fun invoke(recipe: RecipeDomain): Result<RecipeDomain>
}

class UpdateRecipeUseCaseImpl(
    private val recipeRepository: RecipeRepository,
    private val collectionRepository: CollectionRepository
) : UpdateRecipeUseCase {

    override suspend operator fun invoke(recipe: RecipeDomain): Result<RecipeDomain> {
        return recipeRepository.updateRecipe(
            recipeId = recipe.id,
            recipe = recipe,
            collectionIds = recipe.collections.map { it.id }
        )
            .onSuccess {
                collectionRepository.getCollections()
            }
    }
}
