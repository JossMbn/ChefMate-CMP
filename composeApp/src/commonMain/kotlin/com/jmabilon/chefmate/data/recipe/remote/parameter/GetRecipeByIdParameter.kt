package com.jmabilon.chefmate.data.recipe.remote.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeByIdParameter(
    @SerialName("p_recipe_id") val recipeId: String
)
