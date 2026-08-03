package com.jmabilon.chefmate.di

import com.jmabilon.chefmate.core.di.coreModule
import com.jmabilon.chefmate.data.authentication.di.authenticationDataModule
import com.jmabilon.chefmate.data.cookbook.di.cookbookDataModule
import com.jmabilon.chefmate.data.recipe.di.recipeDataModule
import com.jmabilon.chefmate.domain.cookbook.di.cookbookDomainModule
import com.jmabilon.chefmate.feature.account.di.accountModule
import com.jmabilon.chefmate.feature.authentication.signin.di.signInModule
import com.jmabilon.chefmate.feature.authentication.signup.di.signUpModule
import com.jmabilon.chefmate.feature.cookbook.cookbooklist.di.cookbookListModule
import com.jmabilon.chefmate.feature.cookbook.details.di.cookbookDetailsModule
import com.jmabilon.chefmate.feature.cookbook.selection.di.cookbookSelectionModule
import com.jmabilon.chefmate.feature.entrypoint.di.chefMateModule
import com.jmabilon.chefmate.feature.home.di.homeModule
import com.jmabilon.chefmate.feature.overlay.cookbook.create.di.cookbookCreationPresentationModule
import com.jmabilon.chefmate.feature.recipe.creation.di.recipeCreationModule
import com.jmabilon.chefmate.feature.recipe.creation2.di.recipeEditorModule
import com.jmabilon.chefmate.feature.recipe.details.di.recipeDetailsModule
import com.jmabilon.chefmate.feature.recipe.scanner.di.recipeScannerModule
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
            // Core modules
            coreModule,
            // Data modules
            authenticationDataModule,
            cookbookDataModule,
            recipeDataModule,
            // Domain modules
            cookbookDomainModule,
            // feature modules
            chefMateModule,
            homeModule,
            accountModule,
            signInModule,
            signUpModule,
            cookbookListModule,
            cookbookDetailsModule,
            cookbookSelectionModule,
            cookbookCreationPresentationModule,
            recipeEditorModule,
            recipeCreationModule,
            recipeDetailsModule,
            recipeScannerModule
        )
    }
}
