package com.jmabilon.chefmate.application

import android.app.Application
import com.jmabilon.chefmate.application.delegate.koin.KoinApplicationDelegate
import com.jmabilon.chefmate.application.delegate.koin.KoinApplicationDelegateImpl

class ChefMateApplication : Application(),
    KoinApplicationDelegate by KoinApplicationDelegateImpl() {

    override fun onCreate() {
        super.onCreate()
        setupKoin(application = this)
    }
}
