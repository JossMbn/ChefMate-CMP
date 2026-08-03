package com.jmabilon.chefmate.feature.recipe.creation2.presentation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

// ==================================================================================
//  Route
// ==================================================================================

@Serializable
data object RecipeEditorRoute

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface RecipeEditorNavigator {
    fun navigateBack()
}

class RecipeEditorNavigatorImpl(
    private val controller: NavController? = null
) : RecipeEditorNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.recipeEditorPage(
    controller: NavController
) {
    composable<RecipeEditorRoute> {
        val navigator = remember { RecipeEditorNavigatorImpl(controller = controller) }

        RecipeEditorRoot(navigator = navigator)
    }
}
