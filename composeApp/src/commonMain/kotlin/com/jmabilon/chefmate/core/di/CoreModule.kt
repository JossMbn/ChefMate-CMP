package com.jmabilon.chefmate.core.di

import com.jmabilon.chefmate.core.data.cache.DataCache
import com.jmabilon.chefmate.core.data.cache.DataCacheMemory
import com.jmabilon.chefmate.core.data.cache.TimeProvider
import com.jmabilon.chefmate.core.data.cache.TimeProviderImpl
import com.jmabilon.chefmate.core.network.SupabaseFactory
import io.github.jan.supabase.SupabaseClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {

    // =================================================================================================
    // Cache
    // =================================================================================================

    singleOf(::TimeProviderImpl) { bind<TimeProvider>() }
    singleOf(::DataCacheMemory) { bind<DataCache>() }

    // =================================================================================================
    // Network
    // =================================================================================================

    single<SupabaseClient> { SupabaseFactory.createSupabaseClient() }
}
