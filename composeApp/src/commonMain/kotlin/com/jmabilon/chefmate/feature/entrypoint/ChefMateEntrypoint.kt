package com.jmabilon.chefmate.feature.entrypoint

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.domain.authentication.model.AuthenticationStatus
import com.jmabilon.chefmate.feature.entrypoint.content.MainNavHost
import com.jmabilon.chefmate.feature.entrypoint.content.AuthenticationNavHost

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
