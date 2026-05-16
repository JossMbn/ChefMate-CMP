package com.jmabilon.chefmate.feature.home.mapper

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import com.jmabilon.chefmate.feature.home.model.CollectionUiData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class CollectionDomainUiMapper : Mapper<CollectionUiData, CollectionDomain> {

    override fun convert(input: CollectionDomain): CollectionUiData {
        val imageUrls = input.recipes
            .take(3)
            .mapNotNull { it.imageUrl }

        return CollectionUiData(
            id = input.id,
            name = input.name,
            imageUrls = imageUrls.toImmutableList(),
            recipeCount = input.recipeCount,
            systemType = input.systemType
        )
    }
}

// =================================================================================================
// Extensions
// =================================================================================================

fun List<CollectionDomain>.toUiData(): ImmutableList<CollectionUiData> {
    return CollectionDomainUiMapper().convertOrEmpty(this).toImmutableList()
}
