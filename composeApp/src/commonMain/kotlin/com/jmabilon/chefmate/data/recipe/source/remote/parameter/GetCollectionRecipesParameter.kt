package com.jmabilon.chefmate.data.recipe.source.remote.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCollectionRecipesParameter(
    @SerialName("p_collection_id") val collectionId: String
)
