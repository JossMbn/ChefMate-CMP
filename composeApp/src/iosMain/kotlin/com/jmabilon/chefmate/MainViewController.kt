package com.jmabilon.chefmate

import androidx.compose.ui.window.ComposeUIViewController
import com.jmabilon.chefmate.di.initKoin
import com.jmabilon.chefmate.domain.authentication.model.AuthenticationStatus
import com.jmabilon.chefmate.feature.entrypoint.ChefMateEntrypoint

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    ChefMateEntrypoint(
        authenticationStatus = AuthenticationStatus.Initializing
    )
}
