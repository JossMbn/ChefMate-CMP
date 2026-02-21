package com.jmabilon.chefmate.feature.account.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.account.AccountRoot
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

fun NavGraphBuilder.accountPage(
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
