package com.jmabilon.chefmate.feature.authentication.signin.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.authentication.signin.SignInRoot
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
}

class SignInNavigatorImpl(
    private val controller: NavController? = null
) : SignInNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.signInPage(
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
