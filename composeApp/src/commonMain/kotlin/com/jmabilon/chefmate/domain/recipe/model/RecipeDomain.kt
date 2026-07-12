package com.jmabilon.chefmate.domain.recipe.model

data class RecipeDomain(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val prepTime: Int, // en secondes
    val cookTime: Int, // en secondes
    val servings: Int,
    val difficulty: RecipeDifficulty?,
    val mainIngredients: List<RecipeIngredientDomain>,
    val ingredientSections: List<RecipeIngredientSectionDomain>,
    val instructions: List<RecipeInstructionDomain>,
    val cookbooks: List<RecipeCookbookInfoDomain>
)
