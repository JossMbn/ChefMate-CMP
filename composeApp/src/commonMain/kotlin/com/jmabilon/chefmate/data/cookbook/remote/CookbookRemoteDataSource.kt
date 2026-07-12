package com.jmabilon.chefmate.data.cookbook.remote

import com.jmabilon.chefmate.domain.cookbook.model.CookbookDetailsDomain
import com.jmabilon.chefmate.domain.recipe.model.CookbookDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain

interface CookbookRemoteDataSource {

    // =============================================================================================
    // Cookbooks
    // =============================================================================================

    suspend fun getCookbooks(): Result<List<CookbookDomain>>

    suspend fun getCookbookDetails(
        cookbookId: String,
        page: Int
    ): Result<CookbookDetailsDomain>

    suspend fun getCookbookRecipes(cookbookId: String): Result<List<RecipeDomain>>

    suspend fun createCookbook(cookbookName: String): Result<CookbookDomain>

    suspend fun deleteCookbook(cookbookId: String): Result<Unit>

    suspend fun renameCookbook(cookbookId: String, newName: String): Result<Unit>

    suspend fun moveRecipeToCookbooks(recipeId: String, cookbookIds: List<String>): Result<Unit>

    suspend fun toggleRecipeToFavoriteCookbook(recipeId: String): Result<Boolean>
}
