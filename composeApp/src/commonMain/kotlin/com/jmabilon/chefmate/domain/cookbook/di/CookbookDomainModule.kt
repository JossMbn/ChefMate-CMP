package com.jmabilon.chefmate.domain.cookbook.di

import com.jmabilon.chefmate.domain.cookbook.usecase.ObserveCookbookByIdUseCase
import com.jmabilon.chefmate.domain.cookbook.usecase.ObserveCookbookByIdUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val cookbookDomainModule = module {
    factoryOf(::ObserveCookbookByIdUseCaseImpl) { bind<ObserveCookbookByIdUseCase>() }
}
