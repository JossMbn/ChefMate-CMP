package com.jmabilon.chefmate.feature.entrypoint.content

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
import com.jmabilon.chefmate.feature.account.navigation.accountPage
import com.jmabilon.chefmate.feature.collection.details.navigation.collectionDetailsPage
import com.jmabilon.chefmate.feature.collection.selection.navigation.collectionSelectionPage
import com.jmabilon.chefmate.feature.home.navigation.HomeRoute
import com.jmabilon.chefmate.feature.home.navigation.homePage
import com.jmabilon.chefmate.feature.recipe.creation.navigation.manualRecipeCreationPage
import com.jmabilon.chefmate.feature.recipe.details.navigation.recipeDetailsPage
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
            homePage(controller = navController)
            accountPage(controller = navController)

            // Recipe
            manualRecipeCreationPage(controller = navController)
            recipeDetailsPage(controller = navController)

            // Collection
            collectionDetailsPage(controller = navController)
            collectionSelectionPage(controller = navController)
        }
    }
}
