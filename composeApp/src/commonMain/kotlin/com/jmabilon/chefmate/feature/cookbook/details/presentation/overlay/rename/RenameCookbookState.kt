package com.jmabilon.chefmate.feature.cookbook.details.presentation.overlay.rename

data class RenameCookbookState(
    val cookbookName: String = ""
)

data class RenameCookbookInternalState(
    val cookbookName: String? = null
)
