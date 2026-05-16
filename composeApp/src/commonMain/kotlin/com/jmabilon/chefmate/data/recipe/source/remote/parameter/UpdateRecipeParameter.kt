package com.jmabilon.chefmate.data.recipe.source.remote.parameter

import com.jmabilon.chefmate.data.recipe.source.remote.request.CreateOrUpdateRecipeRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRecipeParameter(
    @SerialName("p_recipe_id") val recipeId: String,
    @SerialName("p_recipe") val recipe: CreateOrUpdateRecipeRequest,
    @SerialName("p_collection_ids") val collectionIds: List<String>
)
