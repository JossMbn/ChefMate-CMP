package com.jmabilon.chefmate.domain.collection.di

import com.jmabilon.chefmate.domain.collection.usecase.ObserveCollectionByIdUseCase
import com.jmabilon.chefmate.domain.collection.usecase.ObserveCollectionByIdUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val collectionDomainModule = module {
    factoryOf(::ObserveCollectionByIdUseCaseImpl) { bind<ObserveCollectionByIdUseCase>() }
}
