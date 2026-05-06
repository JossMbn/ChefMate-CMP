package com.jmabilon.chefmate.feature.recipe.details.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.collection.selection.navigation.CollectionSelectionRoute
import com.jmabilon.chefmate.feature.recipe.details.RecipeDetailsRoot
import kotlinx.serialization.Serializable

// ==================================================================================
//  Route
// ==================================================================================

@Serializable
data class RecipeDetailsRoute(
    val recipeId: String
)

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface RecipeDetailsNavigator {
    fun navigateBack()

    fun navigateToCollectionSelection(recipeId: String)
}

class RecipeDetailsNavigatorImpl(
    private val controller: NavController? = null
) : RecipeDetailsNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }

    override fun navigateToCollectionSelection(recipeId: String) {
        controller?.navigate(CollectionSelectionRoute(recipeId = recipeId))
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.recipeDetailsPage(
    controller: NavController
) {
    composable<RecipeDetailsRoute> {
        val navigator = remember { RecipeDetailsNavigatorImpl(controller = controller) }

        RecipeDetailsRoot(navigator = navigator)
    }
}
