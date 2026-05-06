package com.jmabilon.chefmate.domain.recipe.model

data class RecipeIngredientDomain(
    val id: String,
    val name: String,
    val quantity: Double?,
    val unit: String?,
    val note: String?,
    val sortOrder: Int
)
