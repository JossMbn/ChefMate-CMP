package com.jmabilon.chefmate.data.recipe.source.remote.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoveRecipeToCollectionsParameter(
    @SerialName("p_recipe_id") val recipeId: String,
    @SerialName("p_collection_ids") val collectionIds: List<String>
)
