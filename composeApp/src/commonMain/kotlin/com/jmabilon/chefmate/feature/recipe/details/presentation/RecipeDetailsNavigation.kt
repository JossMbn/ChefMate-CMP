package com.jmabilon.chefmate.feature.recipe.details.presentation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.collection.selection.presentation.CollectionSelectionRoute
import com.jmabilon.chefmate.feature.recipe.creation.presentation.ManualRecipeCreationRoute
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

    fun navigateToRecipeEdition(recipeId: String)

    fun navigateToCollectionSelection(recipeId: String)
}

class RecipeDetailsNavigatorImpl(
    private val controller: NavController? = null
) : RecipeDetailsNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }

    override fun navigateToRecipeEdition(recipeId: String) {
        controller?.navigate(ManualRecipeCreationRoute(recipeId = recipeId))
    }

    override fun navigateToCollectionSelection(recipeId: String) {
        controller?.navigate(CollectionSelectionRoute(recipeId = recipeId))
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.recipeDetailsScreen(
    controller: NavController
) {
    composable<RecipeDetailsRoute> {
        val navigator = remember { RecipeDetailsNavigatorImpl(controller = controller) }

        RecipeDetailsRoot(navigator = navigator)
    }
}
