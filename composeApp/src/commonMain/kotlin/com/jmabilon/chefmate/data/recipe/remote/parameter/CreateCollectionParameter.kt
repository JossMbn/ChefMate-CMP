package com.jmabilon.chefmate.data.recipe.remote.parameter

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCollectionParameter(
    @SerialName("p_name") val collectionName: String,
    @SerialName("p_system_type") val systemType: String? = null
)
