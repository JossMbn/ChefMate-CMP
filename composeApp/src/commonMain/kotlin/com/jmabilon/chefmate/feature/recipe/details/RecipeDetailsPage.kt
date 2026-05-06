package com.jmabilon.chefmate.feature.recipe.details

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_rounded_fill
import com.jmabilon.chefmate.designsystem.component.LoadingContentState
import com.jmabilon.chefmate.designsystem.component.appbar.CMTopAppBar
import com.jmabilon.chefmate.designsystem.component.appbar.TopAppBarBackIcon
import com.jmabilon.chefmate.designsystem.component.recipe.RecipeImageWithPlaceHolder
import com.jmabilon.chefmate.designsystem.extension.negativePadding
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.details.component.RecipeInfoContainer
import com.jmabilon.chefmate.feature.recipe.details.component.RecipeIngredientMainContainer
import com.jmabilon.chefmate.feature.recipe.details.component.RecipeIngredientSubContainer
import com.jmabilon.chefmate.feature.recipe.details.component.RecipeInstructionContainer
import com.jmabilon.chefmate.feature.recipe.details.mock.RecipeDetailsPageMockProvider
import com.jmabilon.chefmate.feature.recipe.details.model.RecipeDetailsAction
import com.jmabilon.chefmate.feature.recipe.details.model.RecipeDetailsState
import com.jmabilon.chefmate.feature.recipe.details.navigation.RecipeDetailsNavigator
import com.jmabilon.chefmate.feature.recipe.details.navigation.RecipeDetailsNavigatorImpl
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RecipeDetailsRoot(
    viewModel: RecipeDetailsViewModel = koinViewModel(),
    navigator: RecipeDetailsNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RecipeDetailsPage(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun RecipeDetailsPage(
    state: RecipeDetailsState,
    onAction: (RecipeDetailsAction) -> Unit,
    navigator: RecipeDetailsNavigator
) {
    val defaultContainerColor = MaterialTheme.colorScheme.primary
    var scrollFraction by remember { mutableStateOf(0f) }
    val topAppBarContainerColor by remember {
        derivedStateOf {
            defaultContainerColor.copy(alpha = scrollFraction)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CMTopAppBar(
                navigationIcon = {
                    TopAppBarBackIcon(onClick = navigator::navigateBack)
                },
                actions = {
                    IconButton(
                        onClick = {
                            navigator.navigateToCollectionSelection(
                                recipeId = state.recipeDetails.id
                            )
                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_add_rounded_fill),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topAppBarContainerColor,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Crossfade(targetState = state.loadingContentState) { loadingContentState ->
            when (loadingContentState) {
                LoadingContentState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator(modifier = Modifier.size(60.dp))
                    }
                }

                LoadingContentState.Content -> {
                    RecipeDetailsPageContent(
                        innerPadding = innerPadding,
                        state = state,
                        onAction = onAction,
                        navigator = navigator,
                        onScrollFractionChanged = { fraction ->
                            scrollFraction = fraction
                        }
                    )
                }

                else -> {
                    /* no-op */
                }
            }
        }
    }
}

@Composable
private fun RecipeDetailsPageContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    state: RecipeDetailsState,
    onAction: (RecipeDetailsAction) -> Unit,
    navigator: RecipeDetailsNavigator,
    onScrollFractionChanged: (Float) -> Unit
) {
    val lazyListState = rememberLazyListState()

    val scrollFraction by remember {
        derivedStateOf {
            val offset = lazyListState.firstVisibleItemScrollOffset
            val index = lazyListState.firstVisibleItemIndex

            if (index > 0) 1f else (offset / 800f).coerceIn(0f, 1f)
        }
    }

    val lazyContentPadding = remember(innerPadding) {
        PaddingValues(
            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr) + 16.dp,
            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr) + 16.dp,
            bottom = innerPadding.calculateBottomPadding() + 32.dp
        )
    }

    LaunchedEffect(scrollFraction) {
        onScrollFractionChanged(scrollFraction)
    }

    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
        state = lazyListState,
        contentPadding = lazyContentPadding,
        verticalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        item {
            RecipeImageWithPlaceHolder(
                modifier = Modifier
                    .negativePadding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f),
                imageUrl = state.recipeDetails.imageUrl
            )
        }

        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large),
                text = state.recipeDetails.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (state.recipeDetails.isRecipeInfoVisible) {
            item {
                RecipeInfoContainer(
                    timeInfo = state.recipeDetails.timeInfo,
                    difficultyInfo = state.recipeDetails.difficultyInfo
                )
            }
        }

        items(state.recipeDetails.ingredients.groups) { group ->
            if (group.title == null) {
                RecipeIngredientMainContainer(
                    servings = state.recipeDetails.ingredients.servings,
                    group = group,
                    onServingsChanged = { onAction(RecipeDetailsAction.OnServingsChanged(action = it)) }
                )
            } else {
                RecipeIngredientSubContainer(group = group)
            }
        }

        item {
            RecipeInstructionContainer(
                instructions = state.recipeDetails.instructions
            )
        }
    }
}

@Preview
@Composable
private fun RecipeDetailsPagePreview(
    @PreviewParameter(RecipeDetailsPageMockProvider::class) state: RecipeDetailsState
) {
    ChefMateTheme(isDarkMode = false) {
        RecipeDetailsPage(
            state = state,
            onAction = { /* no-op */ },
            navigator = RecipeDetailsNavigatorImpl()
        )
    }
}
