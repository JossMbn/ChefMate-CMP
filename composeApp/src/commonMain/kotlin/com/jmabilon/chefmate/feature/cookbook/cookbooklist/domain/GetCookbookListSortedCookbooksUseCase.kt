package com.jmabilon.chefmate.feature.cookbook.cookbooklist.domain

import com.jmabilon.chefmate.domain.cookbook.repository.CookbookRepository
import com.jmabilon.chefmate.domain.recipe.model.CookbookDomain
import com.jmabilon.chefmate.domain.recipe.model.CookbookSystemType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetCookbookListSortedCookbooksUseCase {
    operator fun invoke(): Flow<List<CookbookDomain>>
}

class GetCookbookListSortedCookbooksUseCaseImpl(
    private val cookbookRepository: CookbookRepository
) : GetCookbookListSortedCookbooksUseCase {

    override operator fun invoke(): Flow<List<CookbookDomain>> {
        return cookbookRepository.observeCookbooks()
            .map { cookbooks ->
                cookbooks
                    .filter { it.systemType != CookbookSystemType.Favorites }
                    // Uncategorized cookbooks should always be at the top of the list, sorted by updatedAt desc
                    .sortedWith(
                        compareByDescending<CookbookDomain> { it.systemType == CookbookSystemType.Uncategorized }
                            .thenByDescending { it.updatedAt }
                    )
            }
    }
}
