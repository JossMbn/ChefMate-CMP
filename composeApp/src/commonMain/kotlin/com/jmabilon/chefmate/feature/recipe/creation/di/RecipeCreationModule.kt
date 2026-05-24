package com.jmabilon.chefmate.feature.recipe.creation.di

import com.jmabilon.chefmate.feature.recipe.creation.domain.usecase.CreateOrUpdateRecipeWithImageUseCase
import com.jmabilon.chefmate.feature.recipe.creation.domain.usecase.CreateOrUpdateRecipeWithImageUseCaseImpl
import com.jmabilon.chefmate.feature.recipe.creation.domain.usecase.UploadRecipeImageUseCase
import com.jmabilon.chefmate.feature.recipe.creation.domain.usecase.UploadRecipeImageUseCaseImpl
import com.jmabilon.chefmate.feature.recipe.creation.domain.usecase.ValidateAndPrepareRecipeImageUseCase
import com.jmabilon.chefmate.feature.recipe.creation.domain.usecase.createValidateAndPrepareRecipeImageUseCase
import com.jmabilon.chefmate.feature.recipe.creation.presentation.ManualRecipeCreationViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val recipeCreationModule = module {

    // =================================================================================================
    // ViewModels
    // =================================================================================================

    viewModelOf(::ManualRecipeCreationViewModel)

    // =================================================================================================
    // UseCases
    // =================================================================================================

    factoryOf(::CreateOrUpdateRecipeWithImageUseCaseImpl) { bind<CreateOrUpdateRecipeWithImageUseCase>() }
    factoryOf(::UploadRecipeImageUseCaseImpl) { bind<UploadRecipeImageUseCase>() }
    factory { createValidateAndPrepareRecipeImageUseCase() }.bind<ValidateAndPrepareRecipeImageUseCase>()
}
