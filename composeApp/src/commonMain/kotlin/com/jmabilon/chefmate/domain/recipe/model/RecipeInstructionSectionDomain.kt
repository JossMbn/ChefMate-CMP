package com.jmabilon.chefmate.domain.recipe.model

data class RecipeInstructionSectionDomain(
    val name: String,
    val sortOrder: Int,
    val instructions: List<RecipeInstructionDomain>,
)
