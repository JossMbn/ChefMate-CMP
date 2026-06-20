package com.jmabilon.chefmate.feature.recipe.scanner.presentation

enum class RecipeScannerContentView {
    Initializing,
    Scanning
}

data class RecipeScannerState(
    val contentView: RecipeScannerContentView = RecipeScannerContentView.Initializing,
    val scanningType: RecipeScannerType? = null
)
