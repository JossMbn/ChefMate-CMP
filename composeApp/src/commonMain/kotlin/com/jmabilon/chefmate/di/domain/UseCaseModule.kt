package com.jmabilon.chefmate.di.domain

import com.jmabilon.chefmate.domain.authentication.usecase.ObserveAuthenticationStatusUseCase
import com.jmabilon.chefmate.domain.authentication.usecase.SignInWithEmailUseCase
import com.jmabilon.chefmate.domain.authentication.usecase.SignInWithEmailUseCaseImpl
import com.jmabilon.chefmate.domain.authentication.usecase.SignOutUseCase
import com.jmabilon.chefmate.domain.authentication.usecase.SignOutUseCaseImpl
import com.jmabilon.chefmate.domain.authentication.usecase.SignUpWithEmailUseCase
import com.jmabilon.chefmate.domain.authentication.usecase.SignUpWithEmailUseCaseImpl
import com.jmabilon.chefmate.domain.collection.usecase.CreateCollectionUseCase
import com.jmabilon.chefmate.domain.collection.usecase.CreateCollectionUseCaseImpl
import com.jmabilon.chefmate.domain.collection.usecase.DeleteCollectionUseCase
import com.jmabilon.chefmate.domain.collection.usecase.DeleteCollectionUseCaseImpl
import com.jmabilon.chefmate.domain.collection.usecase.ObserveCollectionByIdUseCase
import com.jmabilon.chefmate.domain.collection.usecase.ObserveCollectionByIdUseCaseImpl
import com.jmabilon.chefmate.domain.collection.usecase.ObserveCollectionsUseCase
import com.jmabilon.chefmate.domain.collection.usecase.ObserveCollectionsUseCaseImpl
import com.jmabilon.chefmate.domain.collection.usecase.UpdateRecipeCollectionsUseCase
import com.jmabilon.chefmate.domain.collection.usecase.UpdateRecipeCollectionsUseCaseImpl
import com.jmabilon.chefmate.domain.recipe.usecase.CreateManualRecipeUseCase
import com.jmabilon.chefmate.domain.recipe.usecase.CreateManualRecipeUseCaseImpl
import com.jmabilon.chefmate.domain.recipe.usecase.CreateManualRecipeWithImageUseCase
import com.jmabilon.chefmate.domain.recipe.usecase.CreateManualRecipeWithImageUseCaseImpl
import com.jmabilon.chefmate.domain.recipe.usecase.ObserveRecipeById
import com.jmabilon.chefmate.domain.recipe.usecase.ObserveRecipeByIdImpl
import com.jmabilon.chefmate.domain.recipe.usecase.ObserveRecipeDetailsUseCase
import com.jmabilon.chefmate.domain.recipe.usecase.ObserveRecipeDetailsUseCaseImpl
import com.jmabilon.chefmate.domain.recipe.usecase.UploadRecipeImageUseCase
import com.jmabilon.chefmate.domain.recipe.usecase.UploadRecipeImageUseCaseImpl
import com.jmabilon.chefmate.domain.recipe.usecase.ValidateAndPrepareRecipeImageUseCase
import com.jmabilon.chefmate.domain.recipe.usecase.createValidateAndPrepareRecipeImageUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val useCaseModule = module {

    // =============================================================================================
    // Authentication
    // =============================================================================================

    factoryOf(::ObserveAuthenticationStatusUseCase)
    factoryOf(::SignInWithEmailUseCaseImpl).bind<SignInWithEmailUseCase>()
    factoryOf(::SignUpWithEmailUseCaseImpl).bind<SignUpWithEmailUseCase>()
    factoryOf(::SignOutUseCaseImpl).bind<SignOutUseCase>()

    // =============================================================================================
    // Recipe
    // =============================================================================================

    factoryOf(::CreateManualRecipeWithImageUseCaseImpl).bind<CreateManualRecipeWithImageUseCase>()
    factoryOf(::CreateManualRecipeUseCaseImpl).bind<CreateManualRecipeUseCase>()
    factoryOf(::ObserveRecipeDetailsUseCaseImpl).bind<ObserveRecipeDetailsUseCase>()
    factoryOf(::UploadRecipeImageUseCaseImpl).bind<UploadRecipeImageUseCase>()
    factory { createValidateAndPrepareRecipeImageUseCase() }.bind<ValidateAndPrepareRecipeImageUseCase>()
    factoryOf(::ObserveRecipeByIdImpl).bind<ObserveRecipeById>()
    factoryOf(::UpdateRecipeCollectionsUseCaseImpl).bind<UpdateRecipeCollectionsUseCase>()

    // =============================================================================================
    // Collections
    // =============================================================================================

    factoryOf(::CreateCollectionUseCaseImpl).bind<CreateCollectionUseCase>()
    factoryOf(::DeleteCollectionUseCaseImpl).bind<DeleteCollectionUseCase>()
    factoryOf(::ObserveCollectionsUseCaseImpl).bind<ObserveCollectionsUseCase>()
    factoryOf(::ObserveCollectionByIdUseCaseImpl).bind<ObserveCollectionByIdUseCase>()
}
