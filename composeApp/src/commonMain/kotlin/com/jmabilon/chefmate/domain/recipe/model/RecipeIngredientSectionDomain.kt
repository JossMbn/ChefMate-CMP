package com.jmabilon.chefmate.domain.recipe.model

data class RecipeIngredientSectionDomain(
    val name: String,
    val sortOrder: Int,
    val ingredients: List<RecipeIngredientDomain>
)
