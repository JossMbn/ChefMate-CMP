package com.jmabilon.chefmate.core.network

import com.jmabilon.chefmate.BuildKonfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.ktor.client.plugins.HttpTimeout

object SupabaseFactory {

    @OptIn(SupabaseInternal::class)
    fun createSupabaseClient(): SupabaseClient = io.github.jan.supabase.createSupabaseClient(
        supabaseUrl = BuildKonfig.SUPABASE_URL,
        supabaseKey = BuildKonfig.SUPABASE_API_KEY
    ) {
        httpConfig {
            install(HttpTimeout) {
                requestTimeoutMillis = 45000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 45000
            }
        }

        install(Auth.Companion)
        install(Postgrest.Companion)
        install(Storage.Companion)
        install(Functions.Companion)
    }
}
