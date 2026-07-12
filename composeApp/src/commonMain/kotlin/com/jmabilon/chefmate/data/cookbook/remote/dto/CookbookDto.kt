package com.jmabilon.chefmate.data.cookbook.remote.dto

import com.jmabilon.chefmate.core.common.Mapper
import com.jmabilon.chefmate.core.domain.extension.toLocalDateTime
import com.jmabilon.chefmate.data.recipe.remote.dto.RecipeDto
import com.jmabilon.chefmate.data.recipe.remote.dto.toDomain
import com.jmabilon.chefmate.domain.recipe.model.CookbookDomain
import com.jmabilon.chefmate.domain.recipe.model.CookbookSystemType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CookbookDto(
    val id: String,
    val name: String,
    @SerialName("system_type")
    val systemType: String? = null,
    @SerialName("recipe_count")
    val recipeCount: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val recipes: List<RecipeDto>? = null
)

// =================================================================================================
// Mapper
// =================================================================================================

class CookbookMapper : Mapper<CookbookDomain, CookbookDto> {

    override fun convert(input: CookbookDto): CookbookDomain {
        return CookbookDomain(
            id = input.id,
            name = input.name,
            systemType = CookbookSystemType.fromValue(input.systemType),
            createdAt = input.createdAt.toLocalDateTime(),
            updatedAt = input.updatedAt.toLocalDateTime(),
            recipeCount = input.recipeCount,
            recipes = input.recipes.toDomain()
        )
    }
}
