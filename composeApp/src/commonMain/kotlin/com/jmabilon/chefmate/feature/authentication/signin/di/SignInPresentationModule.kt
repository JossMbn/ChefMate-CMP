package com.jmabilon.chefmate.feature.authentication.signin.di

import com.jmabilon.chefmate.feature.authentication.signin.presentation.SignInViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val signInModule = module {
    viewModelOf(::SignInViewModel)
}
