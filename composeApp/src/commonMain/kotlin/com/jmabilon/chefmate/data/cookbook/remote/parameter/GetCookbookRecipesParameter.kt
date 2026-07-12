package com.jmabilon.chefmate.data.cookbook.remote.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCookbookRecipesParameter(
    @SerialName("p_collection_id") val cookbookId: String
)
