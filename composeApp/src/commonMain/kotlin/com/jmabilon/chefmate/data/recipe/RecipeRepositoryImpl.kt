package com.jmabilon.chefmate.data.recipe

import com.jmabilon.chefmate.data.recipe.source.remote.RecipeRemoteDataSource
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import com.jmabilon.chefmate.domain.recipe.repository.RecipeRepository

class RecipeRepositoryImpl(
    private val recipeRemoteDataSource: RecipeRemoteDataSource
) : RecipeRepository {

    // =============================================================================================
    // Recipe
    // =============================================================================================

    override suspend fun getRecipeById(recipeId: String): Result<RecipeDomain> {
        return recipeRemoteDataSource.getRecipeById(recipeId = recipeId)
    }

    override suspend fun createRecipe(
        recipe: RecipeDomain,
        collectionIds: List<String>
    ): Result<RecipeDomain> {
        return recipeRemoteDataSource.createRecipe(recipe = recipe, collectionIds = collectionIds)
    }

    override suspend fun deleteRecipe(recipeId: String): Result<Unit> {
        return recipeRemoteDataSource.deleteRecipe(recipeId = recipeId)
    }

    override suspend fun updateRecipe(
        recipeId: String,
        recipe: RecipeDomain
    ): Result<RecipeDomain> {
        return recipeRemoteDataSource.updateRecipe(recipeId = recipeId, recipe = recipe)
    }

    // =============================================================================================
    // Collections
    // =============================================================================================

    override suspend fun getCollectionRecipes(collectionId: String): Result<List<RecipeDomain>> {
        return recipeRemoteDataSource.getCollectionRecipes(collectionId = collectionId)
    }

    override suspend fun createCollection(collectionName: String): Result<CollectionDomain> {
        return recipeRemoteDataSource.createCollection(collectionName = collectionName)
    }

    override suspend fun deleteCollection(collectionId: String): Result<Unit> {
        return recipeRemoteDataSource.deleteCollection(collectionId = collectionId)
    }

    override suspend fun updateCollection(
        collectionId: String,
        newName: String
    ): Result<CollectionDomain> {
        return recipeRemoteDataSource.updateCollection(
            collectionId = collectionId,
            newName = newName
        )
    }

    override suspend fun moveRecipeToCollections(
        recipeId: String,
        collectionIds: List<String>
    ): Result<Unit> {
        return recipeRemoteDataSource.moveRecipeToCollections(
            recipeId = recipeId,
            collectionIds = collectionIds
        )
    }
}
