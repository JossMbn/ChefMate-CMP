package com.jmabilon.chefmate.data.cookbook.remote.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoveRecipeToCookbooksParameter(
    @SerialName("p_recipe_id") val recipeId: String,
    @SerialName("p_collection_ids") val cookbookIds: List<String>
)
