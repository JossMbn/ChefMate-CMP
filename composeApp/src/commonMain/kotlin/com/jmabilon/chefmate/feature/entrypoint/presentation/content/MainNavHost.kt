package com.jmabilon.chefmate.feature.entrypoint.presentation.content

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jmabilon.chefmate.core.presentation.ObserveAsEvent
import com.jmabilon.chefmate.core.presentation.SnackbarController
import com.jmabilon.chefmate.feature.account.presentation.accountScreen
import com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.cookbookListScreen
import com.jmabilon.chefmate.feature.cookbook.details.presentation.cookbookDetailsPage
import com.jmabilon.chefmate.feature.cookbook.selection.presentation.cookbookSelectionPage
import com.jmabilon.chefmate.feature.home.presentation.HomeRoute
import com.jmabilon.chefmate.feature.home.presentation.homeScreen
import com.jmabilon.chefmate.feature.recipe.creation.presentation.manualRecipeCreationScreen
import com.jmabilon.chefmate.feature.recipe.details.presentation.recipeDetailsScreen
import com.jmabilon.chefmate.feature.recipe.scanner.presentation.recipeScannerScreen
import kotlinx.coroutines.launch

@Composable
fun MainNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvent(SnackbarController.event) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = event.message.asStringSuspend()
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
        ) {
            homeScreen(controller = navController)
            accountScreen(controller = navController)

            // Recipe
            manualRecipeCreationScreen(controller = navController)
            recipeDetailsScreen(controller = navController)
            recipeScannerScreen(controller = navController)

            // Cookbook
            cookbookListScreen(controller = navController)
            cookbookDetailsPage(controller = navController)
            cookbookSelectionPage(controller = navController)
        }
    }
}
