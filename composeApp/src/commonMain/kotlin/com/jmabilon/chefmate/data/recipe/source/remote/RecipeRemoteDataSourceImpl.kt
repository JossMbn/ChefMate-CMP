package com.jmabilon.chefmate.data.recipe.source.remote

import com.jmabilon.chefmate.core.data.extension.decodeAndMap
import com.jmabilon.chefmate.core.supabase.extension.safeExecution
import com.jmabilon.chefmate.data.recipe.source.remote.dto.RecipeMapper
import com.jmabilon.chefmate.data.recipe.source.remote.model.RecipeRpcFunction
import com.jmabilon.chefmate.data.recipe.source.remote.model.RecipeTable
import com.jmabilon.chefmate.data.recipe.source.remote.model.RecipeTableColumn
import com.jmabilon.chefmate.data.recipe.source.remote.parameter.CreateRecipeParameter
import com.jmabilon.chefmate.data.recipe.source.remote.parameter.GetRecipeByIdParameter
import com.jmabilon.chefmate.data.recipe.source.remote.request.toRequest
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.storage.storage

class RecipeRemoteDataSourceImpl(
    private val supabaseClient: SupabaseClient
) : RecipeRemoteDataSource {

    companion object {
        private const val RECIPE_BUCKET_ID = "recipe-images"
    }

    // =============================================================================================
    // Recipes
    // =============================================================================================

    override suspend fun getRecipeById(recipeId: String): Result<RecipeDomain> {
        val parameters = GetRecipeByIdParameter(recipeId = recipeId)

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = RecipeRpcFunction.GetRecipeById.functionName,
                parameters = parameters
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
                parameters = recipe.toRequest()
            )
                .decodeAndMap(mapper = RecipeMapper())
        }
    }

    // =============================================================================================
    // Images
    // =============================================================================================

    override suspend fun fetchRecipeImageUrl(imagePath: String): Result<String> {
        return supabaseClient.safeExecution {
            storage.from(RECIPE_BUCKET_ID)
                .publicUrl(path = imagePath)
        }
    }

    override suspend fun uploadRecipeImage(
        recipeId: String,
        imageData: ByteArray,
        extension: String
    ): Result<String> {
        val userId = supabaseClient.auth.currentUserOrNull()?.id
            ?: return Result.failure(IllegalStateException("User must be authenticated"))
        val imagePath = "$userId/$recipeId.$extension"

        return supabaseClient.safeExecution {
            storage.from(RECIPE_BUCKET_ID)
                .upload(path = imagePath, data = imageData)
        }
            .map { imagePath }
    }

    override suspend fun deleteRecipeImage(imagePath: String): Result<Unit> {
        return supabaseClient.safeExecution {
            storage.from(RECIPE_BUCKET_ID)
                .delete(imagePath)
        }
    }
}
