package com.jmabilon.chefmate.data.recipe.remote.dto

import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.domain.recipe.model.CollectionSystemType
import com.jmabilon.chefmate.domain.recipe.model.RecipeCollectionInfoDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeCollectionInfoDto(
    val id: String,
    val name: String,
    @SerialName("system_type")
    val systemType: String? = null
)

// =================================================================================================
// Mapper
// =================================================================================================

class RecipeCollectionInfoMapper : Mapper<RecipeCollectionInfoDomain, RecipeCollectionInfoDto> {

    override fun convert(input: RecipeCollectionInfoDto): RecipeCollectionInfoDomain {
        return RecipeCollectionInfoDomain(
            id = input.id,
            name = input.name,
            systemType = CollectionSystemType.fromValue(input.systemType)
        )
    }
}

// =================================================================================================
// Extensions
// =================================================================================================

fun List<RecipeCollectionInfoDto>.toDomain(): List<RecipeCollectionInfoDomain> =
    RecipeCollectionInfoMapper().convert(this)
