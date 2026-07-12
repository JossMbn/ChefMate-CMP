package com.jmabilon.chefmate.domain.recipe.repository

import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    // =============================================================================================
    // Observation
    // =============================================================================================

    fun observeRecipeById(recipeId: String): Flow<RecipeDomain>

    // =============================================================================================
    // CRUD
    // =============================================================================================

    suspend fun createRecipe(recipe: RecipeDomain, cookbookIds: List<String>): Result<RecipeDomain>

    suspend fun deleteRecipe(recipeId: String): Result<Unit>

    suspend fun updateRecipe(recipe: RecipeDomain): Result<RecipeDomain>

    // =============================================================================================
    // Images
    // =============================================================================================

    suspend fun getRecipeImageUrl(imagePath: String): Result<String>

    suspend fun uploadRecipeImage(recipeId: String, imageData: ByteArray, extension: String): Result<String>

    suspend fun deleteRecipeImage(imagePath: String): Result<Unit>

    suspend fun scanRecipeFromImage(imageData: List<Byte>): Result<RecipeDomain>
}
