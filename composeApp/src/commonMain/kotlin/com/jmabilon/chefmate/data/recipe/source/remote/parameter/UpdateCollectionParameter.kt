package com.jmabilon.chefmate.data.recipe.source.remote.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateCollectionParameter(
    @SerialName("name") val collectionName: String,
    @SerialName("system_type") val systemType: String? = null
)
