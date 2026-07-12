package com.jmabilon.chefmate.feature.home.presentation

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.account.presentation.AccountRoute
import com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.CookbookListRoute
import com.jmabilon.chefmate.feature.cookbook.details.presentation.CookbookDetailsRoute
import com.jmabilon.chefmate.feature.recipe.creation.presentation.ManualRecipeCreationRoute
import com.jmabilon.chefmate.feature.recipe.scanner.presentation.RecipeScannerType
import kotlinx.serialization.Serializable

// ==================================================================================
//  Route
// ==================================================================================

@Serializable
data object HomeRoute

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface HomeNavigator {

    fun navigateBack()

    fun navigateToCreateRecipe()

    fun navigateToAccountPage()

    fun navigateToCookbookList()

    fun navigateToCookbookDetails(cookbookId: String)

    fun navigateToRecipeScanner(type: RecipeScannerType)
}

class HomeNavigatorImpl(
    private val controller: NavController? = null
) : HomeNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }

    override fun navigateToCreateRecipe() {
        controller?.navigate(ManualRecipeCreationRoute())
    }

    override fun navigateToAccountPage() {
        controller?.navigate(AccountRoute)
    }

    override fun navigateToCookbookList() {
        controller?.navigate(CookbookListRoute)
    }

    override fun navigateToCookbookDetails(cookbookId: String) {
        controller?.navigate(CookbookDetailsRoute(cookbookId = cookbookId))
    }

    override fun navigateToRecipeScanner(type: RecipeScannerType) {
        //controller?.navigate(RecipeScannerRoute(type = type))
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.homeScreen(
    controller: NavController
) {
    composable<HomeRoute> {
        HomeRoot(
            navigator = HomeNavigatorImpl(
                controller = controller
            )
        )
    }
}
