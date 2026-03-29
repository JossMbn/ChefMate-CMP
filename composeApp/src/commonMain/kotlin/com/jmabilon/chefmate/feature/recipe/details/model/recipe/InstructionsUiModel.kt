package com.jmabilon.chefmate.feature.recipe.details.model.recipe

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class InstructionsUiModel(
    val steps: ImmutableList<StepUiModel> = persistentListOf()
)

data class StepUiModel(
    val number: String, // "1", "2", "3", ...
    val title: String?, // "Préparer la pâte", null if have no title,
    val instruction: String, // "Mélanger la farine, le sucre et le beurre pour faire la pâte"
)
