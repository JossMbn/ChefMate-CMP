package com.jmabilon.chefmate.feature.recipe.details.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_favorite_rounded_fill
import chefmate.composeapp.generated.resources.ic_favorite_rounded_outlined
import com.jmabilon.chefmate.core.designsystem.extension.negativePadding
import com.jmabilon.chefmate.core.designsystem.newcomponent.appbar.CMTopAppBar
import com.jmabilon.chefmate.core.designsystem.newcomponent.button.IconButton
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.AsyncState
import com.jmabilon.chefmate.core.presentation.ObserveAsEvent
import com.jmabilon.chefmate.feature.recipe.details.presentation.component.section.ImageNameSection
import com.jmabilon.chefmate.feature.recipe.details.presentation.component.section.RecipeActionButtonsSection
import com.jmabilon.chefmate.feature.recipe.details.presentation.component.section.ingredient.IngredientsSection
import com.jmabilon.chefmate.feature.recipe.details.presentation.component.section.instruction.InstructionsSection
import com.jmabilon.chefmate.feature.recipe.details.presentation.component.section.quickinfo.QuickInfo
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeServingActionType
import com.jmabilon.chefmate.feature.recipe.details.presentation.model.RecipeUiModel
import com.jmabilon.chefmate.feature.recipe.details.presentation.preview.RecipeDetailsPagePreviewProvider
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RecipeDetailsRoot(
    viewModel: RecipeDetailsViewModel = koinViewModel(),
    navigator: RecipeDetailsNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.event) { event ->
        when (event) {
            RecipeDetailsEvent.RecipeSuccessfullyDeleted -> navigator.navigateBack()
        }
    }

    RecipeDetailsScreen(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun RecipeDetailsScreen(
    state: RecipeDetailsState,
    onAction: (RecipeDetailsAction) -> Unit,
    navigator: RecipeDetailsNavigator
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CMTopAppBar(
                colors = topAppBarColors(containerColor = Color.Transparent),
                onNavigationIconClick = navigator::navigateBack,
                actions = {
                    if (state.recipe.isContent) {
                        IconButton(
                            painter = if (state.isInFavorites) {
                                painterResource(Res.drawable.ic_favorite_rounded_fill)
                            } else {
                                painterResource(Res.drawable.ic_favorite_rounded_outlined)
                            },
                            contentDescription = "Add to favorites",
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            onClick = { onAction(RecipeDetailsAction.OnFavoriteClick) }
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        RecipeDetailsScreenContent(
            innerPadding = innerPadding,
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun RecipeDetailsScreenContent(
    innerPadding: PaddingValues,
    state: RecipeDetailsState,
    onAction: (RecipeDetailsAction) -> Unit,
    navigator: RecipeDetailsNavigator,
) {
    when (val recipe = state.recipe) {
        AsyncState.Loading,
        AsyncState.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator(modifier = Modifier.size(60.dp))
            }
        }

        is AsyncState.Content -> {
            RecipeDetailsContent(
                innerPadding = innerPadding,
                recipe = recipe.data,
                onAction = onAction,
                navigator = navigator
            )
        }
    }
}

@Composable
fun RecipeDetailsContent(
    innerPadding: PaddingValues,
    recipe: RecipeUiModel,
    onAction: (RecipeDetailsAction) -> Unit,
    navigator: RecipeDetailsNavigator
) {
    val lazyListState = rememberLazyListState()

    val lazyContentPadding = remember(innerPadding) {
        PaddingValues(
            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
            bottom = innerPadding.calculateBottomPadding() + 32.dp
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = lazyContentPadding,
        verticalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        item {
            ImageNameSection(
                imageUrl = recipe.imageUrl,
                recipeName = recipe.name
            )
        }

        item {
            RecipeActionButtonsSection(
                onCookModeClick = { navigator.navigateToCookMode(recipeId = recipe.id) },
                onAddToCookbookClick = { navigator.navigateToCookbookSelection(recipeId = recipe.id) },
                onEditClick = { navigator.navigateToRecipeEdition(recipeId = recipe.id) }
            )
        }

        item {
            QuickInfo(quickInfo = recipe.quickInfo)
        }

        item {
            IngredientsSection(
                modifier = Modifier.negativePadding(horizontal = 16.dp),
                servings = recipe.serving,
                ingredientSections = recipe.ingredients,
                onServingDecreased = {
                    onAction(
                        RecipeDetailsAction.OnServingsChanged(
                            RecipeServingActionType.Decrement
                        )
                    )
                },
                onServingIncreased = {
                    onAction(
                        RecipeDetailsAction.OnServingsChanged(
                            RecipeServingActionType.Increment
                        )
                    )
                },
                onAddIngredientClick = { /* no-op */ }
            )
        }

        item {
            InstructionsSection(
                instructions = recipe.instructions,
                onAddInstructionClick = { /* no-op */ }
            )
        }
    }
}

@Preview
@Composable
private fun RecipeDetailsScreenPreview(
    @PreviewParameter(RecipeDetailsPagePreviewProvider::class) state: RecipeDetailsState
) {
    ChefMateTheme(isDarkMode = false) {
        RecipeDetailsScreen(
            state = state,
            onAction = { /* no-op */ },
            navigator = RecipeDetailsNavigatorImpl()
        )
    }
}
