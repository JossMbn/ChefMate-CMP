package com.jmabilon.chefmate.feature.account.di

import com.jmabilon.chefmate.feature.account.presentation.AccountViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val accountModule = module {
    viewModelOf(::AccountViewModel)
}
