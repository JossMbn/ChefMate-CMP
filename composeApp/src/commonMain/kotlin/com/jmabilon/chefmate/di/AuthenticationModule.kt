package com.jmabilon.chefmate.di

import com.jmabilon.chefmate.data.authentication.AuthenticationRepositoryImpl
import com.jmabilon.chefmate.data.authentication.source.remote.AuthenticationRemoteDataSource
import com.jmabilon.chefmate.data.authentication.source.remote.AuthenticationRemoteDataSourceImpl
import com.jmabilon.chefmate.domain.authentication.repository.AuthenticationRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authenticationModule = module {

    // =============================================================================================
    // Data Layer
    // =============================================================================================

    singleOf(::AuthenticationRepositoryImpl).bind<AuthenticationRepository>()
    singleOf(::AuthenticationRemoteDataSourceImpl).bind<AuthenticationRemoteDataSource>()
}
