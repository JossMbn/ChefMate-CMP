package com.jmabilon.chefmate.feature.home.domain.usecase

import com.jmabilon.chefmate.domain.cookbook.repository.CookbookRepository
import com.jmabilon.chefmate.domain.recipe.model.CookbookSystemType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetHomeScreenDataUseCase {
    operator fun invoke(): Flow<HomeScreenData>
}

class GetHomeScreenDataUseCaseImpl(
    private val cookbooksRepository: CookbookRepository
) : GetHomeScreenDataUseCase {

    override operator fun invoke(): Flow<HomeScreenData> {
        return cookbooksRepository.observeCookbooks()
            .map { cookbooks ->
                val favoriteCookbookId = cookbooks
                    .firstOrNull { it.systemType == CookbookSystemType.Favorites }?.id ?: ""

                HomeScreenData(
                    favoriteCookbookId = favoriteCookbookId
                )
            }
    }
}

data class HomeScreenData(
    val favoriteCookbookId: String
)
