package com.jmabilon.chefmate.domain.recipe.model

data class RecipeIngredientSectionDomain(
    val id: String,
    val name: String,
    val sortOrder: Int,
    val ingredients: List<RecipeIngredientDomain>
)
