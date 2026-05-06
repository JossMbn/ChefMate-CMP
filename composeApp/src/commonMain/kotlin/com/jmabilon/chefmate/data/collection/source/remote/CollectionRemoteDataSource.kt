package com.jmabilon.chefmate.data.collection.source.remote

import com.jmabilon.chefmate.domain.collection.model.CollectionDetailsDomain
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import com.jmabilon.chefmate.domain.recipe.model.RecipeDomain

interface CollectionRemoteDataSource {

    // =============================================================================================
    // Collections
    // =============================================================================================

    suspend fun getCollections(): Result<List<CollectionDomain>>

    suspend fun getCollectionDetails(
        collectionId: String,
        page: Int
    ): Result<CollectionDetailsDomain>

    suspend fun getCollectionRecipes(collectionId: String): Result<List<RecipeDomain>>

    suspend fun createCollection(collectionName: String): Result<CollectionDomain>

    suspend fun deleteCollection(collectionId: String): Result<Unit>

    suspend fun updateCollection(collectionId: String, newName: String): Result<CollectionDomain>

    suspend fun moveRecipeToCollections(recipeId: String, collectionIds: List<String>): Result<Unit>
}
