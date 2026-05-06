package com.jmabilon.chefmate.domain.recipe.model

data class RecipeInstructionDomain(
    val id: String,
    val title: String,
    val instructions: String,
    /*val cookDuration: Int?, // en secondes
    val temperature: RecipeTemperatureDomain?,*/ //Not implemented for now
    val sortOrder: Int
)
