package com.jmabilon.chefmate.feature.entrypoint.content

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jmabilon.chefmate.feature.authentication.signin.navigation.signInPage
import com.jmabilon.chefmate.feature.authentication.signup.navigation.signUpPage
import com.jmabilon.chefmate.feature.welcome.navigation.WelcomeRoute
import com.jmabilon.chefmate.feature.welcome.navigation.welcomePage

@Composable
fun AuthenticationNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = WelcomeRoute,
        ) {
            welcomePage(controller = navController)
            signInPage(controller = navController)
            signUpPage(controller = navController)
        }
    }
}
