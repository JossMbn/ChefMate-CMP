package com.jmabilon.chefmate.feature.home.domain.usecase

import com.jmabilon.chefmate.domain.collection.repository.CollectionRepository
import com.jmabilon.chefmate.domain.recipe.model.CollectionDomain
import com.jmabilon.chefmate.domain.recipe.model.CollectionSystemType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetHomeSortedCollectionsUseCase {
    fun invoke(): Flow<List<CollectionDomain>>
}

class GetHomeSortedCollectionsUseCaseImpl(
    private val collectionsRepository: CollectionRepository
) : GetHomeSortedCollectionsUseCase {

    override fun invoke(): Flow<List<CollectionDomain>> {
        return collectionsRepository.observeCollections()
            .map { collections ->
                // Uncategorized and Favorites collections should always be at the top of the list, sorted by updatedAt desc
                collections
                    .sortedWith(
                        compareByDescending<CollectionDomain> { it.systemType == CollectionSystemType.Uncategorized }
                            .thenByDescending { it.systemType == CollectionSystemType.Favorites }
                            .thenByDescending { it.updatedAt }
                    )
            }
    }
}
