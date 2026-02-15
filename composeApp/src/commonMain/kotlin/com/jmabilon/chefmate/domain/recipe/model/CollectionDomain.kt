package com.jmabilon.chefmate.domain.recipe.model

data class CollectionDomain(
    val id: String,
    val name: String,
    val systemType: CollectionSystemType?
)
