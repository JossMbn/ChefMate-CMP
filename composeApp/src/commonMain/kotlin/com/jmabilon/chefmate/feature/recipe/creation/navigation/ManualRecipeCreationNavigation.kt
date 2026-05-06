package com.jmabilon.chefmate.feature.recipe.creation.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.recipe.creation.ManualRecipeCreationRoot
import kotlinx.serialization.Serializable

// ==================================================================================
//  Route
// ==================================================================================

@Serializable
data class ManualRecipeCreationRoute(
    val recipeId: String? = null
)

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface ManualRecipeCreationNavigator {
    fun navigateBack()
}

class ManualRecipeCreationNavigatorImpl(
    private val controller: NavController? = null
) : ManualRecipeCreationNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.manualRecipeCreationPage(
    controller: NavController
) {
    composable<ManualRecipeCreationRoute> {
        ManualRecipeCreationRoot(
            navigator = ManualRecipeCreationNavigatorImpl(controller = controller)
        )
    }
}
