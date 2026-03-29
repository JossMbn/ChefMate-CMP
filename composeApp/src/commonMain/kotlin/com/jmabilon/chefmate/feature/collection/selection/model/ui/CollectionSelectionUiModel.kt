package com.jmabilon.chefmate.feature.collection.selection.model.ui

import com.jmabilon.chefmate.core.domain.Mapper
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain

data class CollectionSelectionUiModel(
    val id: String,
    val name: String,
    val isSelected: Boolean
)

class CollectionSelectionUiModelMapper(
    val selectedCollectionIds: List<String>
) : Mapper<CollectionSelectionUiModel, CollectionDomain> {

    override fun convert(input: CollectionDomain): CollectionSelectionUiModel {
        return CollectionSelectionUiModel(
            id = input.id,
            name = input.name,
            isSelected = selectedCollectionIds.contains(input.id)
        )
    }
}

fun List<CollectionDomain>.toCollectionSelectionUiModel(
    selectedCollectionIds: List<String>
): List<CollectionSelectionUiModel> {
    return CollectionSelectionUiModelMapper(
        selectedCollectionIds = selectedCollectionIds
    ).convert(this)
}
