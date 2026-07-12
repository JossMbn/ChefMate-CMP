package com.jmabilon.chefmate.domain.recipe.model

data class RecipeCookbookInfoDomain(
    val id: String,
    val name: String,
    val systemType: CookbookSystemType?
)
