package com.jmabilon.chefmate.data.recipe.source.remote.dto

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import com.jmabilon.chefmate.domain.recipe.model.CollectionSystemType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDto(
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

class CollectionMapper : Mapper<CollectionDomain, CollectionDto> {

    override fun convert(input: CollectionDto): CollectionDomain {
        return CollectionDomain(
            id = input.id,
            name = input.name,
            systemType = CollectionSystemType.fromValue(input.systemType),
            createdAt = input.createdAt,
            updatedAt = input.updatedAt,
            recipeCount = input.recipeCount,
            recipes = input.recipes.toDomain()
        )
    }
}
