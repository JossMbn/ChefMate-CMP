package com.jmabilon.chefmate.data.recipe.remote

import com.jmabilon.chefmate.core.network.supabase.extension.decodeAndMap
import com.jmabilon.chefmate.core.network.supabase.extension.safeExecution
import com.jmabilon.chefmate.data.recipe.remote.dto.RecipeDto
import com.jmabilon.chefmate.data.recipe.remote.dto.RecipeMapper
import com.jmabilon.chefmate.data.recipe.remote.model.RecipeRpcFunction
import com.jmabilon.chefmate.data.recipe.remote.model.RecipeTable
import com.jmabilon.chefmate.data.recipe.remote.model.RecipeTableColumn
import com.jmabilon.chefmate.data.recipe.remote.parameter.CreateRecipeParameter
import com.jmabilon.chefmate.data.recipe.remote.parameter.GetRecipeByIdParameter
import com.jmabilon.chefmate.data.recipe.remote.parameter.UpdateRecipeParameter
import com.jmabilon.chefmate.data.recipe.remote.request.toRequest
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.storage.storage
import io.ktor.client.call.body
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64

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
        val parameters =
            GetRecipeByIdParameter(
                recipeId = recipeId
            )

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

    override suspend fun updateRecipe(recipe: RecipeDomain): Result<RecipeDomain> {
        val parameters =
            UpdateRecipeParameter(
                recipeId = recipe.id,
                recipe = recipe.toRequest(),
                collectionIds = recipe.collections.map { it.id }
            )

        return supabaseClient.safeExecution {
            postgrest.rpc(
                function = RecipeRpcFunction.UpdateRecipe.functionName,
                parameters = parameters
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

    // =============================================================================================
    // AI Scanning
    // =============================================================================================

    @Serializable
    data class ScanRequest(
        @SerialName("type") val type: String, // "image", "url", "text"
        @SerialName("payload") val payload: ScanPayloadRequest
    )

    @Serializable
    data class ScanPayloadRequest(
        @SerialName("base64") val encodedImage: String? = null,
        @SerialName("mimeType") val mimeType: String? = null,
        @SerialName("url") val url: String? = null,
        @SerialName("text") val text: String? = null
    )

    // TODO : Handle different scanning types (url, text) and their respective payloads
    override suspend fun scanRecipeFromImage(imageData: List<Byte>): Result<RecipeDomain> {
        val encodedImage = Base64.encode(imageData.toByteArray())
        val body = ScanRequest(
            type = "url",
            payload = ScanPayloadRequest(
                encodedImage = encodedImage,
                mimeType = "image/jpeg"
            )
        )

        return supabaseClient.safeExecution {
            val response = functions.invoke(
                function = "scan-recipe-from-image",
                body = body
            )

            RecipeMapper()
                .convert(response.body<RecipeDto>())
        }
    }
}
