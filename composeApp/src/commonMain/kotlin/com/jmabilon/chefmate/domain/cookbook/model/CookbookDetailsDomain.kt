package com.jmabilon.chefmate.domain.cookbook.model

import com.jmabilon.chefmate.domain.recipe.model.CookbookSystemType

data class CookbookDetailsDomain(
    val id: String,
    val title: String,
    val recipeCount: Int,
    val systemType: CookbookSystemType?,
    val recipes: List<CookbookRecipeInfoDomain>
)
