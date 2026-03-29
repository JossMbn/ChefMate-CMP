package com.jmabilon.chefmate.domain.collection.usecase

import com.jmabilon.chefmate.domain.collection.repository.CollectionRepository
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import kotlinx.coroutines.flow.Flow

interface ObserveCollectionsUseCase {
    operator fun invoke(): Flow<List<CollectionDomain>>
}

class ObserveCollectionsUseCaseImpl(
    private val collectionRepository: CollectionRepository
) : ObserveCollectionsUseCase {

    override operator fun invoke(): Flow<List<CollectionDomain>> = collectionRepository.observeCollections()
}
