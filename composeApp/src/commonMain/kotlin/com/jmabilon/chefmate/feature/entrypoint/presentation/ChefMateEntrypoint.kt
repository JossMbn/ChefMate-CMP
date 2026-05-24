package com.jmabilon.chefmate.feature.entrypoint.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.entrypoint.domain.model.AuthenticationStatus
import com.jmabilon.chefmate.feature.entrypoint.presentation.content.AuthenticationNavHost
import com.jmabilon.chefmate.feature.entrypoint.presentation.content.MainNavHost

@Composable
fun ChefMateEntrypoint(
    authenticationStatus: AuthenticationStatus
) {
    ChefMateTheme {
        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
            targetState = authenticationStatus
        ) { targetState ->
            when (targetState) {
                AuthenticationStatus.Authenticated -> MainNavHost()

                AuthenticationStatus.NotAuthenticated,
                AuthenticationStatus.RefreshFailure -> AuthenticationNavHost()

                AuthenticationStatus.Initializing -> Unit
            }
        }
    }
}
