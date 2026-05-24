package com.jmabilon.chefmate.feature.authentication.signin.presentation

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

// ==================================================================================
//  Route
// ==================================================================================

@Serializable
data object SignInRoute

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface SignInNavigator {
    fun navigateBack()

    fun navigateToForgotPassword()
}

class SignInNavigatorImpl(
    private val controller: NavController? = null
) : SignInNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }

    override fun navigateToForgotPassword() {
        // TODO: Implement navigation to forgot password screen
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.signInScreen(
    controller: NavController
) {
    composable<SignInRoute> {
        SignInRoot(
            navigator = SignInNavigatorImpl(
                controller = controller
            )
        )
    }
}
