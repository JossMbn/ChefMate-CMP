package com.jmabilon.chefmate.data.cookbook.remote.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateCookbookParameter(
    @SerialName("name") val cookbookName: String
)
