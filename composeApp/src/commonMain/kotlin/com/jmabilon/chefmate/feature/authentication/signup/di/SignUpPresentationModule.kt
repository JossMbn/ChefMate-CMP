package com.jmabilon.chefmate.feature.authentication.signup.di

import com.jmabilon.chefmate.feature.authentication.signup.presentation.SignUpViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val signUpModule = module {
    viewModelOf(::SignUpViewModel)
}
