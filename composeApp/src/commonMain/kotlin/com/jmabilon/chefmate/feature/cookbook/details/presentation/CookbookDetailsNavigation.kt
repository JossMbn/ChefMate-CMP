package com.jmabilon.chefmate.feature.cookbook.details.presentation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.recipe.details.presentation.RecipeDetailsRoute
import kotlinx.serialization.Serializable

// ==================================================================================
//  Route
// ==================================================================================

@Serializable
data class CookbookDetailsRoute(
    val cookbookId: String
)

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface CookbookDetailsNavigator {

    fun navigateBack()

    fun navigateToRecipeDetails(recipeId: String)
}

class CookbookDetailsNavigatorImpl(
    private val controller: NavController? = null
) : CookbookDetailsNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }

    override fun navigateToRecipeDetails(recipeId: String) {
        controller?.navigate(RecipeDetailsRoute(recipeId = recipeId))
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.cookbookDetailsPage(
    controller: NavController
) {
    composable<CookbookDetailsRoute> {
        val navigator = remember { CookbookDetailsNavigatorImpl(controller = controller) }

        CookbookDetailsRoot(navigator = navigator)
    }
}
