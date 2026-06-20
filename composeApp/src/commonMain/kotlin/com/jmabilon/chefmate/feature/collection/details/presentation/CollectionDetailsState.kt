package com.jmabilon.chefmate.feature.collection.details.presentation

import androidx.compose.runtime.Stable
import com.jmabilon.chefmate.core.designsystem.component.LoadingContentState
import com.jmabilon.chefmate.core.designsystem.component.recipe.model.RecipeCardUiModel
import com.jmabilon.chefmate.domain.recipe.model.CollectionSystemType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed interface CollectionDetailsDialogState {
    data class RenameCollection(val collectionId: String) : CollectionDetailsDialogState
}

data class CollectionDetailsState(
    val loadingContentState: LoadingContentState = LoadingContentState.Loading,
    val collectionTitle: String = "",
    val systemType: CollectionSystemType? = null,
    val recipes: ImmutableList<RecipeCardUiModel> = persistentListOf(),
    val dialogState: CollectionDetailsDialogState? = null
)
