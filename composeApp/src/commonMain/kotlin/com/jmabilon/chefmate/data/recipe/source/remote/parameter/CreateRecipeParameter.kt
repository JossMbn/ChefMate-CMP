package com.jmabilon.chefmate.data.recipe.source.remote.parameter

import com.jmabilon.chefmate.data.recipe.source.remote.request.CreateRecipeRequest
import kotlinx.serialization.SerialName

data class CreateRecipeParameter(
    @SerialName("p_recipe") val recipe: CreateRecipeRequest,
    @SerialName("p_collection_ids") val collectionIds: List<String>
)
