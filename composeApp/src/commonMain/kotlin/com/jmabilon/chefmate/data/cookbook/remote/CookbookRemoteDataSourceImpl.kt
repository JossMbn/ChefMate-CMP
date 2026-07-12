package com.jmabilon.chefmate.data.cookbook.remote

import com.jmabilon.chefmate.core.network.supabase.extension.decodeAndMap
import com.jmabilon.chefmate.core.network.supabase.extension.decodeListAndMap
import com.jmabilon.chefmate.core.network.supabase.extension.safeExecution
import com.jmabilon.chefmate.data.cookbook.remote.dto.CookbookMapper
import com.jmabilon.chefmate.data.cookbook.remote.model.CookbookRpcFunction
import com.jmabilon.chefmate.data.cookbook.remote.model.CookbookTable
import com.jmabilon.chefmate.data.cookbook.remote.model.CookbookTableColumn
import com.jmabilon.chefmate.data.cookbook.remote.parameter.CreateCookbookParameter
import com.jmabilon.chefmate.data.cookbook.remote.parameter.GetCookbookRecipesParameter
import com.jmabilon.chefmate.data.cookbook.remote.parameter.MoveRecipeToCookbooksParameter
import com.jmabilon.chefmate.data.cookbook.remote.parameter.UpdateCookbookParameter
import com.jmabilon.chefmate.data.recipe.remote.dto.RecipeMapper
import com.jmabilon.chefmate.domain.cookbook.model.CookbookDetailsDomain
import com.jmabilon.chefmate.domain.recipe.model.CookbookDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class CookbookRemoteDataSourceImpl(
    private val supabaseClient: SupabaseClient
) : CookbookRemoteDataSource {

    // =============================================================================================
    // Cookbooks
    // =============================================================================================

    override suspend fun getCookbooks(): Result<List<CookbookDomain>> {
        return withContext(Dispatchers.IO) {
            supabaseClient.safeExecution {
                postgrest.rpc(
                    function = CookbookRpcFunction.GetCookbooks.functionName
                )
                    .decodeListAndMap(mapper = CookbookMapper())
            }
        }
    }

    override suspend fun getCookbookDetails(
        cookbookId: String,
        page: Int
    ): Result<CookbookDetailsDomain> {
        /*val offset = page * PAGINATION_LIMIT
        val parameters = GetCollectionDetailsParameter(
            collectionId = collectionId,
            limit = PAGINATION_LIMIT,
            offset = offset
        )

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CollectionRpcFunction.GetCollectionDetails.functionName,
                parameters = parameters
            )
                .decodeAndMap(mapper = CollectionDetailsMapper())
        }*/
        return Result.failure(NotImplementedError("Pagination for cookbook details is not implemented yet"))
    }

    override suspend fun getCookbookRecipes(cookbookId: String): Result<List<RecipeDomain>> {
        val parameters =
            GetCookbookRecipesParameter(cookbookId = cookbookId)

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CookbookRpcFunction.GetRecipesByCookbookId.functionName,
                parameters = parameters
            )
                .decodeListAndMap(mapper = RecipeMapper())
        }
    }

    override suspend fun createCookbook(cookbookName: String): Result<CookbookDomain> {
        val parameters =
            CreateCookbookParameter(cookbookName = cookbookName)

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CookbookRpcFunction.CreateCookbook.functionName,
                parameters = parameters
            )
                .decodeAndMap(mapper = CookbookMapper())
        }
    }

    override suspend fun deleteCookbook(cookbookId: String): Result<Unit> {
        val parameters =
            GetCookbookRecipesParameter(cookbookId = cookbookId)

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CookbookRpcFunction.DeleteCookbook.functionName,
                parameters = parameters
            )
        }
    }

    override suspend fun renameCookbook(
        cookbookId: String,
        newName: String
    ): Result<Unit> {
        val parameters =
            UpdateCookbookParameter(cookbookName = newName)

        return supabaseClient.safeExecution {
            postgrest.from(table = CookbookTable.Cookbooks.tableName)
                .update(value = parameters) {
                    filter {
                        eq(column = CookbookTableColumn.Id.columnName, value = cookbookId)
                    }
                }
        }
    }

    override suspend fun moveRecipeToCookbooks(
        recipeId: String,
        cookbookIds: List<String>
    ): Result<Unit> {
        val parameters = MoveRecipeToCookbooksParameter(
            recipeId = recipeId,
            cookbookIds = cookbookIds
        )

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CookbookRpcFunction.MoveRecipeToCookbooks.functionName,
                parameters = parameters
            )
        }
            .onFailure { error ->
                print("Error moving recipe to collections: ${error.message}")
            }
            .mapCatching { /* no-op */ }
    }

    override suspend fun toggleRecipeToFavoriteCookbook(recipeId: String): Result<Boolean> {
        val parameters = mapOf("p_recipe_id" to recipeId)

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CookbookRpcFunction.ToggleRecipeToFavoriteCookbook.functionName,
                parameters = parameters
            )
                .decodeAs<Boolean>()
        }
    }
}
