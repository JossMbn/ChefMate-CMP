package com.jmabilon.chefmate.feature.collection.details.presentation

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
data class CollectionDetailsRoute(
    val collectionId: String
)

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface CollectionDetailsNavigator {

    fun navigateBack()

    fun navigateToRecipeDetails(recipeId: String)
}

class CollectionDetailsNavigatorImpl(
    private val controller: NavController? = null
) : CollectionDetailsNavigator {

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

fun NavGraphBuilder.collectionDetailsPage(
    controller: NavController
) {
    composable<CollectionDetailsRoute> {
        val navigator = remember { CollectionDetailsNavigatorImpl(controller = controller) }

        CollectionDetailsRoot(navigator = navigator)
    }
}
