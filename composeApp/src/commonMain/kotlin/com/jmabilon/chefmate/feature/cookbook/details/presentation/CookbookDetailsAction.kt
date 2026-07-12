package com.jmabilon.chefmate.feature.cookbook.details.presentation

sealed interface CookbookDetailsAction {
    data object OnRenameCookbookClick : CookbookDetailsAction
    data object OnDeleteCookbookClick : CookbookDetailsAction
    data object OnAddRecipeClick : CookbookDetailsAction
    data object OnDialogDismiss : CookbookDetailsAction
    data class OnFavoriteRecipeClick(val recipeId: String) : CookbookDetailsAction
}
