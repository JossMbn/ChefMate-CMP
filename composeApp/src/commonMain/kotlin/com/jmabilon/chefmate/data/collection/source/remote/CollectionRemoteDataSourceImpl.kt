package com.jmabilon.chefmate.data.collection.source.remote

import com.jmabilon.chefmate.core.data.extension.decodeAndMap
import com.jmabilon.chefmate.core.data.extension.decodeListAndMap
import com.jmabilon.chefmate.core.supabase.extension.safeExecution
import com.jmabilon.chefmate.data.recipe.source.remote.dto.CollectionMapper
import com.jmabilon.chefmate.data.recipe.source.remote.dto.RecipeMapper
import com.jmabilon.chefmate.data.recipe.source.remote.model.CollectionRpcFunction
import com.jmabilon.chefmate.data.recipe.source.remote.model.CollectionTable
import com.jmabilon.chefmate.data.recipe.source.remote.model.CollectionTableColumn
import com.jmabilon.chefmate.data.recipe.source.remote.parameter.CreateCollectionParameter
import com.jmabilon.chefmate.data.recipe.source.remote.parameter.GetCollectionRecipesParameter
import com.jmabilon.chefmate.data.recipe.source.remote.parameter.MoveRecipeToCollectionsParameter
import com.jmabilon.chefmate.data.recipe.source.remote.parameter.UpdateCollectionParameter
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
        val parameters = GetCollectionRecipesParameter(collectionId = collectionId)

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CollectionRpcFunction.GetRecipesByCollectionId.functionName,
                parameters = parameters
            )
                .decodeListAndMap(mapper = RecipeMapper())
        }
    }

    override suspend fun createCollection(collectionName: String): Result<CollectionDomain> {
        val parameters = CreateCollectionParameter(collectionName = collectionName)

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CollectionRpcFunction.CreateCollection.functionName,
                parameters = parameters
            )
                .decodeAndMap(mapper = CollectionMapper())
        }
    }

    override suspend fun deleteCollection(collectionId: String): Result<Unit> {
        val parameters = GetCollectionRecipesParameter(collectionId = collectionId)

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CollectionRpcFunction.DeleteCollection.functionName,
                parameters = parameters
            )
        }
    }

    override suspend fun updateCollection(
        collectionId: String,
        newName: String
    ): Result<CollectionDomain> {
        val parameters = UpdateCollectionParameter(collectionName = newName)

        return supabaseClient.safeExecution {
            postgrest.from(table = CollectionTable.Collections.tableName)
                .update(value = parameters) {
                    filter {
                        eq(column = CollectionTableColumn.Id.columnName, value = collectionId)
                    }
                }
                .decodeAndMap(mapper = CollectionMapper())
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
}
