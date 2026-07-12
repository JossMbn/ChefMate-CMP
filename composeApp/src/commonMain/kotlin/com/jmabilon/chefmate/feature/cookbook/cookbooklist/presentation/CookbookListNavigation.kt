package com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.cookbook.details.presentation.CookbookDetailsRoute
import kotlinx.serialization.Serializable

// ==================================================================================
//  Route
// ==================================================================================

@Serializable
data object CookbookListRoute

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface CookbookListNavigator {

    fun navigateBack()

    fun navigateToCookbookDetails(cookbookId: String)
}

class CookbookListNavigatorImpl(
    private val controller: NavController? = null
) : CookbookListNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }

    override fun navigateToCookbookDetails(cookbookId: String) {
        controller?.navigate(CookbookDetailsRoute(cookbookId = cookbookId))
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.cookbookListScreen(
    controller: NavController
) {
    composable<CookbookListRoute> {
        val navigator = remember { CookbookListNavigatorImpl(controller = controller) }

        CookbookListRoot(navigator = navigator)
    }
}
