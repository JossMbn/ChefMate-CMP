package com.jmabilon.chefmate.core.supabase

import com.jmabilon.chefmate.BuildKonfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseFactory {

    fun createSupabaseClient(): SupabaseClient = createSupabaseClient(
        supabaseUrl = BuildKonfig.SUPABASE_URL,
        supabaseKey = BuildKonfig.SUPABASE_API_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
        install(Functions)
    }
}
