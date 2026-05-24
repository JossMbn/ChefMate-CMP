package com.jmabilon.chefmate.feature.authentication.signup.presentation

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.authentication.signin.presentation.SignInRoute
import kotlinx.serialization.Serializable

// ==================================================================================
//  Route
// ==================================================================================

@Serializable
data object SignUpRoute

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface SignUpNavigator {
    fun navigateBack()

    fun navigateToSignIn()
}

class SignUpNavigatorImpl(
    private val controller: NavController? = null
) : SignUpNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }

    override fun navigateToSignIn() {
        controller?.navigate(SignInRoute)
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.signUpScreen(
    controller: NavController
) {
    composable<SignUpRoute> {
        SignUpRoot(
            navigator = SignUpNavigatorImpl(
                controller = controller
            )
        )
    }
}
