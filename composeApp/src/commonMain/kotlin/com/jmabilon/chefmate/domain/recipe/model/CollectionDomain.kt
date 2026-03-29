package com.jmabilon.chefmate.domain.recipe.model

data class CollectionDomain(
    val id: String,
    val name: String,
    val systemType: CollectionSystemType?,
    val createdAt: String,
    val updatedAt: String,
    val recipeCount: Int,
    val recipes: List<RecipeDomain>
)
