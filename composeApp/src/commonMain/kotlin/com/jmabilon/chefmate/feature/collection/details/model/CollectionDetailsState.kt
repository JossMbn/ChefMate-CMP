package com.jmabilon.chefmate.feature.collection.details.model

import com.jmabilon.chefmate.designsystem.component.LoadingContentState
import com.jmabilon.chefmate.designsystem.component.recipe.model.RecipeCardUiModel
import com.jmabilon.chefmate.domain.recipe.model.CollectionSystemType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CollectionDetailsState(
    val loadingContentState: LoadingContentState = LoadingContentState.Loading,
    val collectionTitle: String = "",
    val systemType: CollectionSystemType? = null,
    val recipes: ImmutableList<RecipeCardUiModel> = persistentListOf()
)
