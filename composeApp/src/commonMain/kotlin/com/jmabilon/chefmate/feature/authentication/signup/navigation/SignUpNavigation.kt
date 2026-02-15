package com.jmabilon.chefmate.feature.authentication.signup.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.authentication.signin.navigation.SignInRoute
import com.jmabilon.chefmate.feature.authentication.signup.SignUpRoot
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

    fun navigateToSignInPage()
}

class SignUpNavigatorImpl(
    private val controller: NavController? = null
) : SignUpNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }

    override fun navigateToSignInPage() {
        controller?.navigate(SignInRoute)
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.signUpPage(
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
