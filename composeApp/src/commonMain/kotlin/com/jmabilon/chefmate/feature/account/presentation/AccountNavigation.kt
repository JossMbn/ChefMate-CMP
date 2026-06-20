package com.jmabilon.chefmate.feature.account.presentation

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

// ==================================================================================
//  Route
// ==================================================================================

@Serializable
data object AccountRoute

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface AccountNavigator {
    fun navigateBack()
}

class AccountNavigatorImpl(
    private val controller: NavController? = null
) : AccountNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.accountScreen(
    controller: NavController
) {
    composable<AccountRoute> {
        AccountRoot(
            navigator = AccountNavigatorImpl(
                controller = controller
            )
        )
    }
}
