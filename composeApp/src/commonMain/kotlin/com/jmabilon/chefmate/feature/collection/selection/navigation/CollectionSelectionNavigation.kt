package com.jmabilon.chefmate.feature.collection.selection.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jmabilon.chefmate.feature.collection.selection.CollectionSelectionRoot
import kotlinx.serialization.Serializable

// ==================================================================================
//  Route
// ==================================================================================

@Serializable
data class CollectionSelectionRoute(
    val recipeId: String
)

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface CollectionSelectionNavigator {
    fun navigateBack()
}

class CollectionSelectionNavigatorImpl(
    private val controller: NavController?
) : CollectionSelectionNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.collectionSelectionPage(
    controller: NavController
) {
    val animationSpec = tween<IntOffset>(durationMillis = 600)

    composable<CollectionSelectionRoute>(
        enterTransition = { slideInVertically(animationSpec = animationSpec) { it } },
        exitTransition = { slideOutVertically(animationSpec = animationSpec) { -it } },
        popExitTransition = { slideOutVertically(animationSpec = animationSpec) { it } }
    ) {
        val navigator = remember { CollectionSelectionNavigatorImpl(controller = controller) }

        CollectionSelectionRoot(navigator = navigator)
    }
}
