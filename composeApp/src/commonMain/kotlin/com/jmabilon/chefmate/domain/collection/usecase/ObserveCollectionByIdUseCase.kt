package com.jmabilon.chefmate.domain.collection.usecase

import com.jmabilon.chefmate.domain.collection.repository.CollectionRepository
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

interface ObserveCollectionByIdUseCase {
    operator fun invoke(collectionId: String): Flow<CollectionDomain>
}

class ObserveCollectionByIdUseCaseImpl(
    private val collectionRepository: CollectionRepository
) : ObserveCollectionByIdUseCase {

    override fun invoke(collectionId: String): Flow<CollectionDomain> =
        collectionRepository.observeCollections()
            .mapNotNull { collections ->
                collections.find { it.id == collectionId }
            }
}
