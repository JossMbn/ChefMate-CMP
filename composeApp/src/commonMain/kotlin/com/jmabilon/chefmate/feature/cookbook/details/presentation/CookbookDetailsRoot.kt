package com.jmabilon.chefmate.feature.cookbook.details.presentation

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
import com.jmabilon.chefmate.core.designsystem.component.button.DropdownMenuItemView
import com.jmabilon.chefmate.core.designsystem.component.button.MoreOptionsMenuButton
import com.jmabilon.chefmate.core.designsystem.newcomponent.appbar.CMTopAppBar
import com.jmabilon.chefmate.core.designsystem.newcomponent.recipe.RecipeCard
import com.jmabilon.chefmate.core.designsystem.newcomponent.recipe.model.RecipeCardUiModel
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.AsyncState
import com.jmabilon.chefmate.core.presentation.ObserveAsEvent
import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.core.presentation.extension.plus
import com.jmabilon.chefmate.feature.cookbook.details.presentation.component.content.CookbookDetailsEmptyContent
import com.jmabilon.chefmate.feature.cookbook.details.presentation.component.content.CookbookDetailsLoadingContent
import com.jmabilon.chefmate.feature.cookbook.details.presentation.overlay.rename.RenameCookbookBottomSheet
import com.jmabilon.chefmate.feature.overlay.addrecipe.AddRecipeBottomSheet
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CookbookDetailsRoot(
    viewModel: CookbookDetailsViewModel = koinViewModel(),
    navigator: CookbookDetailsNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.event) { event ->
        when (event) {
            CookbookDetailsEvent.OnCookbookDeleted -> navigator.navigateBack()
        }
    }

    CookbookDetailsScreen(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@Composable
private fun CookbookDetailsScreen(
    state: CookbookDetailsState,
    onAction: (CookbookDetailsAction) -> Unit,
    navigator: CookbookDetailsNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CMTopAppBar(
                title = state.cookbookTitle,
                onNavigationIconClick = navigator::navigateBack,
                actions = {
                    if (!state.recipes.isLoading && !state.isSystemCookbook) {
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
                                        onAction(CookbookDetailsAction.OnRenameCookbookClick)
                                        hideMenu()
                                    }
                                )

                                DropdownMenuItemView(
                                    menuTitle = stringResource(Res.string.delete) + " cookbook",
                                    colors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.error
                                    ),
                                    onClick = {
                                        onAction(CookbookDetailsAction.OnDeleteCookbookClick)
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
        CookbookDetailsScreenContent(
            innerPadding = innerPadding,
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }

    state.dialogState?.let { dialogState ->
        when (dialogState) {
            is CookbookDetailsDialogState.RenameCookbook -> {
                RenameCookbookBottomSheet(
                    cookbookId = dialogState.cookbookId,
                    onDismissRequest = { onAction(CookbookDetailsAction.OnDialogDismiss) }
                )
            }

            CookbookDetailsDialogState.AddRecipe -> {
                AddRecipeBottomSheet(
                    onDismissRequest = { onAction(CookbookDetailsAction.OnDialogDismiss) },
                    onCreateFromScratch = { /* no-op */ },
                    onScanFromCameraClick = { /* no-op */ },
                    onScanFromUrlClick = { /* no-op */ },
                    onScanFromTextClick = { /* no-op */ }
                )
            }
        }
    }
}

@Composable
private fun CookbookDetailsScreenContent(
    innerPadding: PaddingValues,
    state: CookbookDetailsState,
    onAction: (CookbookDetailsAction) -> Unit,
    navigator: CookbookDetailsNavigator
) {
    when (val recipes = state.recipes) {
        AsyncState.Loading -> {
            CookbookDetailsLoadingContent(innerPadding = innerPadding)
        }

        AsyncState.Failure -> {
            CookbookDetailsEmptyContent(
                innerPadding = innerPadding,
                onAddRecipeClick = { onAction(CookbookDetailsAction.OnAddRecipeClick) }
            )
        }

        is AsyncState.Content -> {
            CookbookDetailsContent(
                innerPadding = innerPadding,
                recipes = recipes.data,
                onAction = onAction,
                navigator = navigator
            )
        }
    }
}

@Composable
private fun CookbookDetailsContent(
    innerPadding: PaddingValues,
    recipes: ImmutableList<RecipeCardUiModel>,
    onAction: (CookbookDetailsAction) -> Unit,
    navigator: CookbookDetailsNavigator
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
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items = recipes, key = { it.id }) { recipe ->
            RecipeCard(
                name = recipe.name,
                imageUrl = recipe.imageUrl,
                prepTimeMinute = recipe.prepTimeMinute,
                isFavorite = recipe.isFavorite,
                onFavoriteClick = { onAction(CookbookDetailsAction.OnFavoriteRecipeClick(recipeId = recipe.id)) },
                onClick = { navigator.navigateToRecipeDetails(recipeId = recipe.id) }
            )
        }
    }
}

@Preview
@Composable
private fun CookbookDetailsScreenPreview() {
    ChefMateTheme {
        CookbookDetailsScreen(
            state = CookbookDetailsState(
                cookbookTitle = "My Cookbook",
                recipes = AsyncState.Content(
                    persistentListOf(
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
                )
            ),
            onAction = { /* no-op */ },
            navigator = CookbookDetailsNavigatorImpl()
        )
    }
}
