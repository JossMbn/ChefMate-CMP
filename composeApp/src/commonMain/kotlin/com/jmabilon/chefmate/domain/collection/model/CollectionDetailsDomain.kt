package com.jmabilon.chefmate.domain.collection.model

import com.jmabilon.chefmate.domain.recipe.model.CollectionSystemType

data class CollectionDetailsDomain(
    val id: String,
    val title: String,
    val recipeCount: Int,
    val systemType: CollectionSystemType?,
    val recipes: List<CollectionRecipeInfoDomain>
)
