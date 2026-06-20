package com.jmabilon.chefmate.data.recipe.remote.parameter

import com.jmabilon.chefmate.data.recipe.remote.request.CreateOrUpdateRecipeRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRecipeParameter(
    @SerialName("p_recipe") val recipe: CreateOrUpdateRecipeRequest,
    @SerialName("p_collection_ids") val collectionIds: List<String>
)
