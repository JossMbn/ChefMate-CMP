package com.jmabilon.chefmate.data.recipe.source.remote

import com.jmabilon.chefmate.core.data.extension.decodeAndMap
import com.jmabilon.chefmate.core.data.extension.decodeListAndMap
import com.jmabilon.chefmate.core.supabase.extension.safeExecution
import com.jmabilon.chefmate.data.recipe.source.remote.dto.CollectionMapper
import com.jmabilon.chefmate.data.recipe.source.remote.dto.RecipeMapper
import com.jmabilon.chefmate.data.recipe.source.remote.model.CollectionRpcFunction
import com.jmabilon.chefmate.data.recipe.source.remote.model.CollectionTable
import com.jmabilon.chefmate.data.recipe.source.remote.model.CollectionTableColumn
import com.jmabilon.chefmate.data.recipe.source.remote.model.RecipeRpcFunction
import com.jmabilon.chefmate.data.recipe.source.remote.model.RecipeTable
import com.jmabilon.chefmate.data.recipe.source.remote.model.RecipeTableColumn
import com.jmabilon.chefmate.data.recipe.source.remote.parameter.CreateCollectionParameter
import com.jmabilon.chefmate.data.recipe.source.remote.parameter.CreateRecipeParameter
import com.jmabilon.chefmate.data.recipe.source.remote.parameter.GetCollectionRecipesParameter
import com.jmabilon.chefmate.data.recipe.source.remote.parameter.GetRecipeByIdParameter
import com.jmabilon.chefmate.data.recipe.source.remote.parameter.MoveRecipeToCollectionsParameter
import com.jmabilon.chefmate.data.recipe.source.remote.parameter.UpdateCollectionParameter
import com.jmabilon.chefmate.data.recipe.source.remote.request.toRequest
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc

class RecipeRemoteDataSourceImpl(
    private val supabaseClient: SupabaseClient
) : RecipeRemoteDataSource {

    // =============================================================================================
    // Recipes
    // =============================================================================================

    override suspend fun getRecipeById(recipeId: String): Result<RecipeDomain> {
        val parameters = GetRecipeByIdParameter(recipeId = recipeId)

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = RecipeRpcFunction.GetRecipeById.functionName, parameters = parameters
            ).decodeAndMap(mapper = RecipeMapper())
        }
    }

    override suspend fun createRecipe(
        recipe: RecipeDomain,
        collectionIds: List<String>
    ): Result<RecipeDomain> {
        val parameters = CreateRecipeParameter(
            recipe = recipe.toRequest(),
            collectionIds = collectionIds
        )

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = RecipeRpcFunction.CreateRecipe.functionName,
                parameters = parameters
            )
                .decodeAndMap(mapper = RecipeMapper())
        }
    }

    override suspend fun deleteRecipe(recipeId: String): Result<Unit> {
        return supabaseClient.safeExecution {
            postgrest.from(table = RecipeTable.Recipes.tableName)
                .delete {
                    filter {
                        eq(column = RecipeTableColumn.Id.columnName, value = recipeId)
                    }
                }
        }
    }

    override suspend fun updateRecipe(
        recipeId: String,
        recipe: RecipeDomain
    ): Result<RecipeDomain> {
        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = RecipeRpcFunction.UpdateRecipe.functionName,
                parameters = recipe
            )
                .decodeAndMap(mapper = RecipeMapper())
        }
    }

    // =============================================================================================
    // Collections
    // =============================================================================================

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
            postgrest.from(table = CollectionTable.Collections.tableName)
                .insert(value = parameters)
                .decodeAndMap(mapper = CollectionMapper())
        }
    }

    override suspend fun deleteCollection(collectionId: String): Result<Unit> {
        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CollectionRpcFunction.GetRecipesByCollectionId.functionName,
                parameters = GetCollectionRecipesParameter(collectionId = collectionId)
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
        recipeId: String, collectionIds: List<String>
    ): Result<Unit> {
        val parameters = MoveRecipeToCollectionsParameter(
            recipeId = recipeId, collectionIds = collectionIds
        )

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = CollectionRpcFunction.MoveRecipeToCollections.functionName,
                parameters = parameters
            )
        }
    }
}
