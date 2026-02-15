package com.jmabilon.chefmate.di.presentation

import com.jmabilon.chefmate.feature.account.AccountViewModel
import com.jmabilon.chefmate.feature.authentication.signin.SignInViewModel
import com.jmabilon.chefmate.feature.authentication.signup.SignUpViewModel
import com.jmabilon.chefmate.feature.entrypoint.ChefMateEntrypointViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    // =============================================================================================
    // Application
    // =============================================================================================

    viewModelOf(::ChefMateEntrypointViewModel)

    // =============================================================================================
    // Authentication
    // =============================================================================================

    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)

    // =============================================================================================
    // Account
    // =============================================================================================

    viewModelOf(::AccountViewModel)
}
