package com.jmabilon.chefmate.feature.collection.selection.model

import com.jmabilon.chefmate.designsystem.component.LoadingContentState
import com.jmabilon.chefmate.feature.collection.selection.model.ui.CollectionSelectionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CollectionSelectionState(
    val loadingContentState: LoadingContentState = LoadingContentState.Loading,
    val collections: ImmutableList<CollectionSelectionUiModel> = persistentListOf()
)
