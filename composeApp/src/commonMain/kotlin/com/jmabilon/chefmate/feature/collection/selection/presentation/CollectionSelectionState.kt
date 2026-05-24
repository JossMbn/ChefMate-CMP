package com.jmabilon.chefmate.feature.collection.selection.presentation

import com.jmabilon.chefmate.core.designsystem.component.LoadingContentState
import com.jmabilon.chefmate.feature.collection.selection.presentation.model.CollectionSelectionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CollectionSelectionState(
    val loadingContentState: LoadingContentState = LoadingContentState.Loading,
    val collections: ImmutableList<CollectionSelectionUiModel> = persistentListOf()
)
