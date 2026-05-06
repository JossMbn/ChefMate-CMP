package com.jmabilon.chefmate.data.recipe.source.remote.dto

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.collection.model.CollectionRecipeInfoDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionRecipeInfoDto(
    val id: String,
    val title: String,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("prep_time_seconds")
    val prepTimeSeconds: Int
)

// =================================================================================================
// Mapper
// =================================================================================================

class CollectionRecipeInfoMapper : Mapper<CollectionRecipeInfoDomain, CollectionRecipeInfoDto> {

    override fun convert(input: CollectionRecipeInfoDto): CollectionRecipeInfoDomain {
        return CollectionRecipeInfoDomain(
            id = input.id,
            title = input.title,
            imageUrl = input.imageUrl,
            prepTimeMinute = input.prepTimeSeconds / 60 // Convert seconds to minutes
        )
    }
}

// =================================================================================================
// Extensions
// =================================================================================================

fun List<CollectionRecipeInfoDto>?.toDomain(): List<CollectionRecipeInfoDomain> {
    return CollectionRecipeInfoMapper().convertOrEmpty(this)
}
