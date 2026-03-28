package com.jmabilon.chefmate

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmabilon.chefmate.di.initKoin
import com.jmabilon.chefmate.domain.authentication.model.AuthenticationStatus
import com.jmabilon.chefmate.feature.entrypoint.ChefMateEntrypoint
import com.jmabilon.chefmate.feature.entrypoint.ChefMateEntrypointViewModel
import org.koin.compose.viewmodel.koinViewModel

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    val viewModel = koinViewModel<ChefMateEntrypointViewModel>()

    val authStatus by viewModel.authStatus.collectAsStateWithLifecycle()
    val isReady by remember {
        derivedStateOf {
            authStatus != AuthenticationStatus.Initializing
        }
    }

    if (isReady) {
        ChefMateEntrypoint(authenticationStatus = authStatus)
    }
}
