package com.jmabilon.chefmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmabilon.chefmate.domain.authentication.model.AuthenticationStatus
import com.jmabilon.chefmate.feature.entrypoint.ChefMateEntrypoint
import com.jmabilon.chefmate.feature.entrypoint.ChefMateEntrypointViewModel
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        setContent {
            val viewModel = koinViewModel<ChefMateEntrypointViewModel>()

            val authStatus by viewModel.authStatus.collectAsStateWithLifecycle()
            val isReady by remember {
                derivedStateOf {
                    authStatus != AuthenticationStatus.Initializing
                }
            }

            splashScreen.setKeepOnScreenCondition { !isReady }

            if (isReady) {
                ChefMateEntrypoint(authenticationStatus = authStatus)
            }
        }
    }
}
