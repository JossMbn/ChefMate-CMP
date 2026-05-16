package com.jmabilon.chefmate.feature.collection.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.delete
import chefmate.composeapp.generated.resources.ic_more_vert_rounded_fill
import chefmate.composeapp.generated.resources.rename
import com.jmabilon.chefmate.core.presentation.ObserveAsEvent
import com.jmabilon.chefmate.core.presentation.extension.plus
import com.jmabilon.chefmate.designsystem.component.LoadingContentState
import com.jmabilon.chefmate.designsystem.component.appbar.CMTopAppBar
import com.jmabilon.chefmate.designsystem.component.appbar.TopAppBarBackIcon
import com.jmabilon.chefmate.designsystem.component.button.DropdownMenuItemView
import com.jmabilon.chefmate.designsystem.component.button.MoreOptionsMenuButton
import com.jmabilon.chefmate.designsystem.component.recipe.RecipeCardItem
import com.jmabilon.chefmate.designsystem.component.recipe.model.RecipeCardUiModel
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.designsystem.utils.UiText
import com.jmabilon.chefmate.feature.collection.details.model.CollectionDetailsAction
import com.jmabilon.chefmate.feature.collection.details.model.CollectionDetailsDialogState
import com.jmabilon.chefmate.feature.collection.details.model.CollectionDetailsEvent
import com.jmabilon.chefmate.feature.collection.details.model.CollectionDetailsState
import com.jmabilon.chefmate.feature.collection.details.navigation.CollectionDetailsNavigator
import com.jmabilon.chefmate.feature.collection.details.navigation.CollectionDetailsNavigatorImpl
import com.jmabilon.chefmate.feature.collection.details.sheet.rename.RenameCollectionBottomSheet
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CollectionDetailsRoot(
    viewModel: CollectionDetailsViewModel = koinViewModel(),
    navigator: CollectionDetailsNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.event) { event ->
        when (event) {
            CollectionDetailsEvent.OnCollectionDeleted -> navigator.navigateBack()
        }
    }

    CollectionDetailsPage(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@Composable
private fun CollectionDetailsPage(
    state: CollectionDetailsState,
    onAction: (CollectionDetailsAction) -> Unit,
    navigator: CollectionDetailsNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CMTopAppBar(
                title = state.collectionTitle,
                navigationIcon = {
                    TopAppBarBackIcon(onClick = navigator::navigateBack)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    if (state.loadingContentState is LoadingContentState.Content && state.systemType == null) {
                        MoreOptionsMenuButton(
                            painter = painterResource(Res.drawable.ic_more_vert_rounded_fill),
                            contentDescription = null,
                            options = { hideMenu ->
                                DropdownMenuItemView(
                                    menuTitle = stringResource(Res.string.rename),
                                    colors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    onClick = {
                                        onAction(CollectionDetailsAction.OnRenameCollectionClick)
                                        hideMenu()
                                    }
                                )

                                DropdownMenuItemView(
                                    menuTitle = stringResource(Res.string.delete),
                                    colors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.error
                                    ),
                                    onClick = {
                                        onAction(CollectionDetailsAction.OnDeleteCollectionClick)
                                        hideMenu()
                                    }
                                )
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        CollectionDetailsPageContent(
            innerPadding = innerPadding,
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }

    state.dialogState?.let { dialogState ->
        when (dialogState) {
            is CollectionDetailsDialogState.RenameCollection -> {
                RenameCollectionBottomSheet(
                    collectionId = dialogState.collectionId,
                    onDismissRequest = { onAction(CollectionDetailsAction.OnDialogDismiss) }
                )
            }
        }
    }
}

@Composable
private fun CollectionDetailsPageContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    state: CollectionDetailsState,
    onAction: (CollectionDetailsAction) -> Unit,
    navigator: CollectionDetailsNavigator
) {
    val contentPadding = remember(innerPadding) {
        innerPadding + PaddingValues(16.dp)
    }
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        state = gridState,
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items = state.recipes, key = { it.id }) { recipe ->
            RecipeCardItem(
                name = recipe.name,
                imageUrl = recipe.imageUrl,
                prepTimeMinute = recipe.prepTimeMinute,
                isFavorite = recipe.isFavorite,
                onFavoriteClick = { onAction(CollectionDetailsAction.OnFavoriteRecipeClick(recipeId = recipe.id)) },
                onClick = { navigator.navigateToRecipeDetails(recipeId = recipe.id) }
            )
        }
    }
}

@Preview
@Composable
private fun CollectionDetailsPagePreview() {
    ChefMateTheme {
        CollectionDetailsPage(
            state = CollectionDetailsState(
                loadingContentState = LoadingContentState.Content,
                collectionTitle = "My Collection",
                recipes = persistentListOf(
                    RecipeCardUiModel(
                        id = "1",
                        name = "Spaghetti Bolognese",
                        imageUrl = "https://example.com/spaghetti.jpg",
                        prepTimeMinute = UiText.DynamicString("30 mins"),
                        isFavorite = false
                    ),
                    RecipeCardUiModel(
                        id = "2",
                        name = "Chicken Curry",
                        imageUrl = "https://example.com/chicken_curry.jpg",
                        prepTimeMinute = UiText.DynamicString("45 mins"),
                        isFavorite = false
                    ),
                    RecipeCardUiModel(
                        id = "3",
                        name = "Vegetable Stir Fry",
                        imageUrl = "https://example.com/vegetable_stir_fry.jpg",
                        prepTimeMinute = UiText.Empty,
                        isFavorite = true
                    ),
                    RecipeCardUiModel(
                        id = "4",
                        name = "Beef Tacos",
                        imageUrl = "https://example.com/beef_tacos.jpg",
                        prepTimeMinute = null,
                        isFavorite = false
                    )
                )
            ),
            onAction = { /* no-op */ },
            navigator = CollectionDetailsNavigatorImpl()
        )
    }
}
