package com.jmabilon.chefmate.data.collection.remote

import com.jmabilon.chefmate.core.network.supabase.extension.decodeAndMap
import com.jmabilon.chefmate.core.network.supabase.extension.decodeListAndMap
import com.jmabilon.chefmate.core.network.supabase.extension.safeExecution
import com.jmabilon.chefmate.data.recipe.remote.dto.CollectionMapper
import com.jmabilon.chefmate.data.recipe.remote.dto.RecipeMapper
import com.jmabilon.chefmate.data.recipe.remote.model.CollectionRpcFunction
import com.jmabilon.chefmate.data.recipe.remote.model.CollectionTable
import com.jmabilon.chefmate.data.recipe.remote.model.CollectionTableColumn
import com.jmabilon.chefmate.data.recipe.remote.parameter.CreateCollectionParameter
import com.jmabilon.chefmate.data.recipe.remote.parameter.GetCollectionRecipesParameter
import com.jmabilon.chefmate.data.recipe.remote.parameter.MoveRecipeToCollectionsParameter
import com.jmabilon.chefmate.data.recipe.remote.parameter.UpdateCollectionParameter
import com.jmabilon.chefmate.domain.collection.model.CollectionDetailsDomain
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc

class CollectionRemoteDataSourceImpl(
    private val supabaseClient: SupabaseClient
) : CollectionRemoteDataSource {

    // =============================================================================================
    // Collections
    // =============================================================================================

    override suspend fun getCollections(): Result<List<CollectionDomain>> {
        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CollectionRpcFunction.GetCollections.functionName
            )
                .decodeListAndMap(mapper = CollectionMapper())
        }
    }

    override suspend fun getCollectionDetails(
        collectionId: String,
        page: Int
    ): Result<CollectionDetailsDomain> {
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
        return Result.failure(NotImplementedError("Pagination for collection details is not implemented yet"))
    }

    override suspend fun getCollectionRecipes(collectionId: String): Result<List<RecipeDomain>> {
        val parameters =
            GetCollectionRecipesParameter(collectionId = collectionId)

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CollectionRpcFunction.GetRecipesByCollectionId.functionName,
                parameters = parameters
            )
                .decodeListAndMap(mapper = RecipeMapper())
        }
    }

    override suspend fun createCollection(collectionName: String): Result<CollectionDomain> {
        val parameters =
            CreateCollectionParameter(collectionName = collectionName)

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CollectionRpcFunction.CreateCollection.functionName,
                parameters = parameters
            )
                .decodeAndMap(mapper = CollectionMapper())
        }
    }

    override suspend fun deleteCollection(collectionId: String): Result<Unit> {
        val parameters =
            GetCollectionRecipesParameter(collectionId = collectionId)

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CollectionRpcFunction.DeleteCollection.functionName,
                parameters = parameters
            )
        }
    }

    override suspend fun renameCollection(
        collectionId: String,
        newName: String
    ): Result<Unit> {
        val parameters =
            UpdateCollectionParameter(collectionName = newName)

        return supabaseClient.safeExecution {
            postgrest.from(table = CollectionTable.Collections.tableName)
                .update(value = parameters) {
                    filter {
                        eq(column = CollectionTableColumn.Id.columnName, value = collectionId)
                    }
                }
        }
    }

    override suspend fun moveRecipeToCollections(
        recipeId: String,
        collectionIds: List<String>
    ): Result<Unit> {
        val parameters = MoveRecipeToCollectionsParameter(
            recipeId = recipeId,
            collectionIds = collectionIds
        )

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CollectionRpcFunction.MoveRecipeToCollections.functionName,
                parameters = parameters
            )
        }
            .onFailure { error ->
                print("Error moving recipe to collections: ${error.message}")
            }
            .mapCatching { /* no-op */ }
    }

    override suspend fun toggleRecipeToFavoriteCollection(recipeId: String): Result<Boolean> {
        val parameters = mapOf("p_recipe_id" to recipeId)

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CollectionRpcFunction.ToggleRecipeToFavoriteCollection.functionName,
                parameters = parameters
            )
                .decodeAs<Boolean>()
        }
    }
}
