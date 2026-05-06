package com.jmabilon.chefmate.feature.home.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.account.navigation.AccountRoute
import com.jmabilon.chefmate.feature.collection.details.navigation.CollectionDetailsRoute
import com.jmabilon.chefmate.feature.home.HomeRoot
import com.jmabilon.chefmate.feature.recipe.creation.navigation.ManualRecipeCreationRoute
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

    fun navigateToCreateRecipePage()

    fun navigateToAccountPage()

    fun navigateToCollectionDetailsPage(collectionId: String)
}

class HomeNavigatorImpl(
    private val controller: NavController? = null
) : HomeNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }

    override fun navigateToCreateRecipePage() {
        controller?.navigate(ManualRecipeCreationRoute)
    }

    override fun navigateToAccountPage() {
        controller?.navigate(AccountRoute)
    }

    override fun navigateToCollectionDetailsPage(collectionId: String) {
        controller?.navigate(CollectionDetailsRoute(collectionId = collectionId))
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.homePage(
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
