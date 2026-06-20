package com.jmabilon.chefmate.data.recipe.remote.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCollectionParameter(
    @SerialName("p_limit") val limit: Int,
    @SerialName("p_offset") val offset: Int = 0
)
