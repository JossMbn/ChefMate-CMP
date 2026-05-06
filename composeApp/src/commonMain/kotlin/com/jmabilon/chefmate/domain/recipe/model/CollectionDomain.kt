package com.jmabilon.chefmate.domain.recipe.model

import kotlinx.datetime.LocalDateTime

data class CollectionDomain(
    val id: String,
    val name: String,
    val systemType: CollectionSystemType?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val recipeCount: Int,
    val recipes: List<RecipeDomain>
)
