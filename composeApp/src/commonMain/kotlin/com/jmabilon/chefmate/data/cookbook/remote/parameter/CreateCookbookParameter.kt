package com.jmabilon.chefmate.data.cookbook.remote.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCookbookParameter(
    @SerialName("p_name") val cookbookName: String,
    @SerialName("p_system_type") val systemType: String? = null
)
