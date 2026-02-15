package com.jmabilon.chefmate.feature.authentication.signup.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
}

class SignUpNavigatorImpl(
    private val controller: NavController? = null
) : SignUpNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.signUpPage(
    controller: NavController
) {
    composable<SignUpRoute> {
        _root_ide_package_.com.jmabilon.chefmate.feature.authentication.signup.SignUpRoot(
            navigator = SignUpNavigatorImpl(
                controller = controller
            )
        )
    }
}
