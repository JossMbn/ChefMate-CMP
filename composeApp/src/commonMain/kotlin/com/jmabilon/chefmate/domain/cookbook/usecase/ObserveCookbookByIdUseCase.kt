package com.jmabilon.chefmate.domain.cookbook.usecase

import com.jmabilon.chefmate.domain.cookbook.repository.CookbookRepository
import com.jmabilon.chefmate.domain.recipe.model.CookbookDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

interface ObserveCookbookByIdUseCase {
    operator fun invoke(cookbookId: String): Flow<CookbookDomain>
}

class ObserveCookbookByIdUseCaseImpl(
    private val cookbookRepository: CookbookRepository
) : ObserveCookbookByIdUseCase {

    override fun invoke(cookbookId: String): Flow<CookbookDomain> =
        cookbookRepository.observeCookbooks()
            .mapNotNull { cookbook ->
                cookbook.find { it.id == cookbookId }
            }
}
