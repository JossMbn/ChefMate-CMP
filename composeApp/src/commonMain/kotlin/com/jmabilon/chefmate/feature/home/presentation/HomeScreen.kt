package com.jmabilon.chefmate.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.extension.plus
import com.jmabilon.chefmate.feature.home.presentation.component.HomeHeader
import com.jmabilon.chefmate.feature.home.presentation.component.HomeQuickAccess
import com.jmabilon.chefmate.feature.home.presentation.component.HomeSearchBar
import com.jmabilon.chefmate.feature.overlay.addrecipe.AddRecipeBottomSheet
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    navigator: HomeNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    navigator: HomeNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HomeHeader(
                onSettingsClick = navigator::navigateToAccountPage
            )
        }
    ) { innerPadding ->
        HomeScreenContent(
            innerPadding = innerPadding,
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }

    state.dialogState?.let { dialog ->
        when (dialog) {
            HomeDialogState.ScanRecipe -> {
                AddRecipeBottomSheet(
                    onDismissRequest = { onAction(HomeAction.OnDismissDialog) },
                    onCreateFromScratch = navigator::navigateToRecipeEditor,
                    onScanFromCameraClick = { /* no-op */ },
                    onScanFromTextClick = { /* no-op */ },
                    onScanFromUrlClick = { /* no-op */ }
                )
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    innerPadding: PaddingValues,
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    navigator: HomeNavigator
) {
    val contentPadding = remember(innerPadding) {
        innerPadding + PaddingValues(vertical = 8.dp, horizontal = 24.dp)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = contentPadding,
    ) {
        item {
            HomeSearchBar(
                modifier = Modifier.fillMaxSize(),
                onClick = { /* no-op */ }
            )
        }

        item {
            HomeQuickAccess(
                onAddClick = { onAction(HomeAction.OnScanRecipeClick) },
                onCookbooksClick = navigator::navigateToCookbookList,
                onFavoritesClick = {
                    state.favoriteCookbookId.takeIf { it.isNotEmpty() }?.let { favoriteCookbookId ->
                        navigator.navigateToCookbookDetails(favoriteCookbookId)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    ChefMateTheme {
        HomeScreen(
            state = HomeState(),
            onAction = { /* no-op */ },
            navigator = HomeNavigatorImpl()
        )
    }
}
