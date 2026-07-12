package com.jmabilon.chefmate.feature.cookbook.selection.presentation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

// ==================================================================================
//  Route
// ==================================================================================

@Serializable
data class CookbookSelectionRoute(
    val recipeId: String
)

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface CookbookSelectionNavigator {
    fun navigateBack()
}

class CookbookSelectionNavigatorImpl(
    private val controller: NavController?
) : CookbookSelectionNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.cookbookSelectionPage(
    controller: NavController
) {
    val animationSpec = tween<IntOffset>(durationMillis = 600)

    composable<CookbookSelectionRoute>(
        enterTransition = { slideInHorizontally(animationSpec = animationSpec) { it } },
        exitTransition = { slideOutHorizontally(animationSpec = animationSpec) { -it } },
        popExitTransition = { slideOutHorizontally(animationSpec = animationSpec) { it } }
    ) {
        val navigator = remember { CookbookSelectionNavigatorImpl(controller = controller) }

        CookbookSelectionRoot(navigator = navigator)
    }
}
