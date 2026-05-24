package com.jmabilon.chefmate.data.recipe.remote.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCollectionDetailsParameter(
    @SerialName("p_collection_id") val collectionId: String,
    @SerialName("p_limit") val limit: Int,
    @SerialName("p_offset") val offset: Int
)
