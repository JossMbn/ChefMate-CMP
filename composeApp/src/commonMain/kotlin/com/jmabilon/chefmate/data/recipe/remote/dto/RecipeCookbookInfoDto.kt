package com.jmabilon.chefmate.data.recipe.remote.dto

import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.domain.recipe.model.CookbookSystemType
import com.jmabilon.chefmate.domain.recipe.model.RecipeCookbookInfoDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeCookbookInfoDto(
    val id: String,
    val name: String,
    @SerialName("system_type")
    val systemType: String? = null
)

// =================================================================================================
// Mapper
// =================================================================================================

class RecipeCookbookInfoMapper : Mapper<RecipeCookbookInfoDomain, RecipeCookbookInfoDto> {

    override fun convert(input: RecipeCookbookInfoDto): RecipeCookbookInfoDomain {
        return RecipeCookbookInfoDomain(
            id = input.id,
            name = input.name,
            systemType = CookbookSystemType.fromValue(input.systemType)
        )
    }
}

// =================================================================================================
// Extensions
// =================================================================================================

fun List<RecipeCookbookInfoDto>.toDomain(): List<RecipeCookbookInfoDomain> =
    RecipeCookbookInfoMapper().convert(this)
