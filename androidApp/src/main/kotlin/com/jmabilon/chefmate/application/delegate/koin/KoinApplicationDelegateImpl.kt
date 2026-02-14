package com.jmabilon.chefmate.application.delegate.koin

import com.jmabilon.chefmate.application.ChefMateApplication
import com.jmabilon.chefmate.di.initKoin
import org.koin.android.ext.koin.androidContext

class KoinApplicationDelegateImpl : KoinApplicationDelegate {

    override fun setupKoin(application: ChefMateApplication) {
        initKoin {
            androidContext(androidContext = application)
        }
    }
}
