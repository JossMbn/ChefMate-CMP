package com.jmabilon.chefmate.domain.recipe.model

data class RecipeIngredientDomain(
    val name: String,
    val quantity: Double?,
    val unit: String?,
    val preparationNotes: String?,
    val sortOrder: Int
)
