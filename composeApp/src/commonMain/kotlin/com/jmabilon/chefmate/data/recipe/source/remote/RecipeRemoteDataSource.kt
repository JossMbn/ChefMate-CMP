package com.jmabilon.chefmate.data.recipe.source.remote

import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain

interface RecipeRemoteDataSource {

    // =============================================================================================
    // Recipe
    // =============================================================================================

    suspend fun getRecipeById(recipeId: String): Result<RecipeDomain>

    suspend fun createRecipe(
        recipe: RecipeDomain,
        collectionIds: List<String>
    ): Result<RecipeDomain>

    suspend fun deleteRecipe(recipeId: String): Result<Unit>

    suspend fun updateRecipe(
        recipeId: String,
        recipe: RecipeDomain,
        collectionIds: List<String>
    ): Result<RecipeDomain>

    // =============================================================================================
    // Images
    // =============================================================================================

    suspend fun fetchRecipeImageUrl(imagePath: String): Result<String>

    suspend fun uploadRecipeImage(
        recipeId: String,
        imageData: ByteArray,
        extension: String
    ): Result<String>

    suspend fun deleteRecipeImage(imagePath: String): Result<Unit>
}
