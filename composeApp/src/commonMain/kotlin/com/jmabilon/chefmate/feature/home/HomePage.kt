package com.jmabilon.chefmate.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_person_rounded_outlined
import com.jmabilon.chefmate.core.presentation.extension.plus
import com.jmabilon.chefmate.designsystem.component.recipe.RecipeCollectionCardItem
import com.jmabilon.chefmate.designsystem.extension.negativePadding
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.home.component.HomeCreationFloatingActionMenu
import com.jmabilon.chefmate.feature.home.model.HomeAction
import com.jmabilon.chefmate.feature.home.model.HomeDialogState
import com.jmabilon.chefmate.feature.home.model.HomeState
import com.jmabilon.chefmate.feature.home.navigation.HomeNavigator
import com.jmabilon.chefmate.feature.home.navigation.HomeNavigatorImpl
import com.jmabilon.chefmate.feature.home.sheet.CreateCollectionBottomSheet
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    navigator: HomeNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomePage(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun HomePage(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    navigator: HomeNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Home") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                actions = {
                    IconButton(onClick = navigator::navigateToAccountPage) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_person_rounded_outlined),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            HomeCreationFloatingActionMenu(
                modifier = Modifier.negativePadding(16.dp),
                onNewCollectionClick = { onAction(HomeAction.OnNewCollectionClick) },
                onFromScratchRecipeClick = navigator::navigateToCreateRecipePage,
                onScanRecipeClick = { /* no-op */ }
            )
        }
    ) { innerPadding ->
        HomePageContentPage(
            innerPadding = innerPadding,
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }
}

@Composable
private fun HomePageContentPage(
    innerPadding: PaddingValues,
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    navigator: HomeNavigator
) {
    val contentPadding = remember(innerPadding) {
        innerPadding + PaddingValues(16.dp)
    }
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        state = gridState,
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(state.collections) { collection ->
            RecipeCollectionCardItem(
                name = collection.displayName.asStringComposable(),
                imageUrl = collection.imageUrls,
                recipeCount = collection.recipeCount,
                onClick = { navigator.navigateToCollectionDetailsPage(collectionId = collection.id) }
            )
        }
    }

    state.dialogState?.let { dialog ->
        when (dialog) {
            HomeDialogState.CreateCollection -> {
                CreateCollectionBottomSheet(
                    onDismissRequest = { onAction(HomeAction.OnDismissDialog) },
                    onCreateCollectionClick = { collectionName ->
                        onAction(HomeAction.OnCreateCollection(collectionName))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomePagePreview() {
    ChefMateTheme {
        HomePage(
            state = HomeState(),
            onAction = { /* no-op */ },
            navigator = HomeNavigatorImpl()
        )
    }
}
