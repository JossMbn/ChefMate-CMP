package com.jmabilon.chefmate.data.authentication.di

import com.jmabilon.chefmate.data.authentication.AuthenticationRepositoryImpl
import com.jmabilon.chefmate.data.authentication.remote.AuthenticationRemoteDataSource
import com.jmabilon.chefmate.data.authentication.remote.AuthenticationRemoteDataSourceImpl
import com.jmabilon.chefmate.domain.authentication.repository.AuthenticationRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authenticationDataModule = module {
    singleOf(::AuthenticationRepositoryImpl).bind<AuthenticationRepository>()
    singleOf(::AuthenticationRemoteDataSourceImpl).bind<AuthenticationRemoteDataSource>()
}
