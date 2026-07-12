package com.jmabilon.chefmate.data.cookbook.remote.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCookbookParameter(
    @SerialName("p_limit") val limit: Int,
    @SerialName("p_offset") val offset: Int = 0
)
