package com.jmabilon.chefmate.domain.collection.usecase

import com.jmabilon.chefmate.domain.collection.repository.CollectionRepository

interface DeleteCollectionUseCase {
    suspend operator fun invoke(collectionId: String): Result<Unit>
}

class DeleteCollectionUseCaseImpl(
    private val collectionRepository: CollectionRepository
) : DeleteCollectionUseCase {

    override suspend fun invoke(collectionId: String): Result<Unit> {
        return collectionRepository.deleteCollection(collectionId)
            .onSuccess {
                collectionRepository.getCollections()
            }
    }
}
