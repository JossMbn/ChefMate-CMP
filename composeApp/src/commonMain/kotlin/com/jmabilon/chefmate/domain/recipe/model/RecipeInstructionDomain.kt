package com.jmabilon.chefmate.domain.recipe.model

data class RecipeInstructionDomain(
    val title: String,
    val instructions: String,
    val cookDuration: Int?, // en secondes
    val temperature: RecipeTemperatureDomain?,
    val sortOrder: Int
)
