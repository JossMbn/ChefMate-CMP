package com.jmabilon.chefmate.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Initializes Koin with the provided configuration and modules.
 *
 * @param config An optional KoinAppDeclaration for additional configuration.
 * Uses in the Android part to provide Android Context.
 */
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            supabaseModule,
            authenticationModule,
            recipeModule
        )
    }
}
