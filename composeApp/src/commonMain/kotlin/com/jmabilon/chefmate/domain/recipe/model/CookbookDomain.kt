package com.jmabilon.chefmate.domain.recipe.model

import kotlinx.datetime.LocalDateTime

data class CookbookDomain(
    val id: String,
    val name: String,
    val systemType: CookbookSystemType?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val recipeCount: Int,
    val recipes: List<RecipeDomain>
)
