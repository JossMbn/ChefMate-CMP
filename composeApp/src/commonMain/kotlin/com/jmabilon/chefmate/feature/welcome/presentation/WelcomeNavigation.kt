package com.jmabilon.chefmate.feature.welcome.presentation

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.authentication.signin.presentation.SignInRoute
import com.jmabilon.chefmate.feature.authentication.signup.presentation.SignUpRoute
import kotlinx.serialization.Serializable

// =================================================================================================
//  Route
// =================================================================================================

@Serializable
data object WelcomeRoute

// =================================================================================================
//  Navigator
// =================================================================================================

@Stable
interface WelcomeNavigator {
    fun navigateToOnBoarding()
    fun navigateToSignIn()
}

class WelcomeNavigatorImpl(
    private val controller: NavController? = null
) : WelcomeNavigator {

    override fun navigateToOnBoarding() {
        controller?.navigate(SignUpRoute)
    }

    override fun navigateToSignIn() {
        controller?.navigate(SignInRoute)
    }
}

// =================================================================================================
//  Graph extension
// =================================================================================================

fun NavGraphBuilder.welcomeScreen(
    controller: NavController
) {
    composable<WelcomeRoute> {
        WelcomeRoot(
            navigator = WelcomeNavigatorImpl(
                controller = controller
            )
        )
    }
}
