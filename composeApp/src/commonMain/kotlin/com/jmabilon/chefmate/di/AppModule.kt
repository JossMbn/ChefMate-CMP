package com.jmabilon.chefmate.di

import com.jmabilon.chefmate.core.supabase.SupabaseFactory
import io.github.jan.supabase.SupabaseClient
import org.koin.dsl.module

val appModule = module {

    // =============================================================================================
    // Supabase
    // =============================================================================================

    single<SupabaseClient> { SupabaseFactory.createSupabaseClient() }
}
