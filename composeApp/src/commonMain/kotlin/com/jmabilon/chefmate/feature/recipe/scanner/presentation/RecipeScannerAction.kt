package com.jmabilon.chefmate.feature.recipe.scanner.presentation

sealed interface RecipeScannerAction {
    data class OnImagePick(val image: List<Byte>?) : RecipeScannerAction
}
