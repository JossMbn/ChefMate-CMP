package com.jmabilon.chefmate.domain.cookbook.model

data class CookbookRecipeInfoDomain(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val prepTimeMinute: Int
)
