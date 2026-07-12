package com.jmabilon.chefmate.data.cookbook.remote.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCookbookDetailsParameter(
    @SerialName("p_collection_id") val cookbookId: String,
    @SerialName("p_limit") val limit: Int,
    @SerialName("p_offset") val offset: Int
)
