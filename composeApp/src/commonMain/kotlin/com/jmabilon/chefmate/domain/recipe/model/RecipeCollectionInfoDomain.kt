package com.jmabilon.chefmate.domain.recipe.model

data class RecipeCollectionInfoDomain(
    val id: String,
    val name: String,
    val systemType: CollectionSystemType?
)
