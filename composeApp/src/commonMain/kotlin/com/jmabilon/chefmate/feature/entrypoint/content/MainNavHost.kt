package com.jmabilon.chefmate.feature.entrypoint.content

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jmabilon.chefmate.core.presentation.ObserveAsEvent
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.feature.account.navigation.AccountRoute
import com.jmabilon.chefmate.feature.account.navigation.accountPage
import kotlinx.coroutines.launch

@Composable
fun MainNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvent(SnackbarController.event) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = event.message.asStringSuspend()
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        NavHost(
            navController = navController,
            startDestination = AccountRoute,
        ) {
            accountPage(controller = navController)
        }
    }
}
