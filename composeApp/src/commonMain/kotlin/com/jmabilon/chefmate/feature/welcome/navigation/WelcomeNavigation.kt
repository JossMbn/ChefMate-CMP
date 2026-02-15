package com.jmabilon.chefmate.feature.welcome.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.authentication.signin.navigation.SignInRoute
import com.jmabilon.chefmate.feature.authentication.signup.navigation.SignUpRoute
import com.jmabilon.chefmate.feature.welcome.WelcomeRoot
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

fun NavGraphBuilder.welcomePage(
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
