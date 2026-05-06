package com.jmabilon.chefmate.feature.collection.details.model

sealed interface CollectionDetailsAction {
    data object OnRenameCollectionClick : CollectionDetailsAction
    data object OnDeleteCollectionClick : CollectionDetailsAction
    data object OnDialogDismiss : CollectionDetailsAction
    data class OnFavoriteRecipeClick(val recipeId: String) : CollectionDetailsAction
}
