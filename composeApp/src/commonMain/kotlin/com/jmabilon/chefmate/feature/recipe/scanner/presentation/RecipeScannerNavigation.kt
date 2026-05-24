package com.jmabilon.chefmate.feature.recipe.scanner.presentation

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
data class RecipeScannerRoute(
    val type: RecipeScannerType
)

enum class RecipeScannerType {
    ImageScan
}

// ==================================================================================
//  Navigator
// ==================================================================================

@Stable
interface RecipeScannerNavigator {
    fun navigateBack()
}

class RecipeScannerNavigatorImpl(
    private val controller: NavController? = null
) : RecipeScannerNavigator {

    override fun navigateBack() {
        controller?.navigateUp()
    }
}

// ==================================================================================
//  Graph extension
// ==================================================================================

fun NavGraphBuilder.recipeScannerScreen(
    controller: NavController
) {
    composable<RecipeScannerRoute> {
        val navigator = remember { RecipeScannerNavigatorImpl(controller = controller) }

        RecipeScannerRoot(navigator = navigator)
    }
}
