package com.jmabilon.chefmate.domain.collection.model

data class CollectionRecipeInfoDomain(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val prepTimeMinute: Int
)
