package com.jmabilon.chefmate.domain.collection.usecase

import com.jmabilon.chefmate.domain.collection.repository.CollectionRepository
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain

interface CreateCollectionUseCase {
    suspend operator fun invoke(collectionName: String): Result<CollectionDomain>
}

class CreateCollectionUseCaseImpl(
    private val collectionRepository: CollectionRepository
) : CreateCollectionUseCase {

    override suspend fun invoke(collectionName: String): Result<CollectionDomain> {
        return collectionRepository.createCollection(collectionName)
    }
}
