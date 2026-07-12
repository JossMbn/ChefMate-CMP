package com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_rounded_fill
import com.jmabilon.chefmate.core.designsystem.newcomponent.appbar.CMTopAppBar
import com.jmabilon.chefmate.core.designsystem.newcomponent.button.IconButton
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.AsyncState
import com.jmabilon.chefmate.core.presentation.UiText
import com.jmabilon.chefmate.core.presentation.extension.plus
import com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.component.CookbookItem
import com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.component.content.CookbookListEmptyContent
import com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.component.content.CookbookListLoadingContent
import com.jmabilon.chefmate.feature.cookbook.cookbooklist.presentation.model.CookbookUiModel
import com.jmabilon.chefmate.feature.overlay.cookbook.create.presentation.CookbookCreationBottomSheet
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CookbookListRoot(
    viewModel: CookbookListViewModel = koinViewModel(),
    navigator: CookbookListNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CookbookListScreen(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@Composable
private fun CookbookListScreen(
    state: CookbookListState,
    onAction: (CookbookListAction) -> Unit,
    navigator: CookbookListNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CMTopAppBar(
                title = "Cookbooks",
                onNavigationIconClick = navigator::navigateBack,
                actions = {
                    if (state.cookbooks.isContent) {
                        IconButton(
                            painter = painterResource(Res.drawable.ic_add_rounded_fill),
                            contentDescription = "Add Cookbook",
                            onClick = { onAction(CookbookListAction.OnAddCookbookClick) }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        CookBookListScreenContent(
            innerPadding = innerPadding,
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }

    state.dialogState?.let { dialogState ->
        when (dialogState) {
            is CookbookListDialogState.CreateCookbook -> {
                CookbookCreationBottomSheet(
                    onDismissRequest = { onAction(CookbookListAction.OnDismissDialog) }
                )
            }
        }
    }
}

@Composable
private fun CookBookListScreenContent(
    innerPadding: PaddingValues,
    state: CookbookListState,
    onAction: (CookbookListAction) -> Unit,
    navigator: CookbookListNavigator
) {
    when (val cookbooks = state.cookbooks) {
        is AsyncState.Loading -> {
            CookbookListLoadingContent(
                innerPadding = innerPadding
            )
        }

        is AsyncState.Failure -> {
            CookbookListEmptyContent(
                innerPadding = innerPadding,
                onAddCookbookClick = { onAction(CookbookListAction.OnAddCookbookClick) }
            )
        }

        is AsyncState.Content -> {
            CookbookListContent(
                innerPadding = innerPadding,
                cookbooks = cookbooks.data,
                navigator = navigator
            )
        }
    }
}

@Composable
private fun CookbookListContent(
    innerPadding: PaddingValues,
    cookbooks: ImmutableList<CookbookUiModel>,
    navigator: CookbookListNavigator
) {
    val contentPadding = remember(innerPadding) {
        innerPadding + PaddingValues(16.dp)
    }

    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        state = gridState,
        columns = GridCells.Fixed(2),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(cookbooks, key = { it.id }) { cookbook ->
            CookbookItem(
                modifier = Modifier.animateItem(),
                name = cookbook.name.asStringComposable(),
                imageUrl = cookbook.imageUrls,
                recipeCount = cookbook.recipeCount,
                onClick = { navigator.navigateToCookbookDetails(cookbookId = cookbook.id) }
            )
        }
    }
}

@Preview
@Composable
private fun CookbookListScreenPreview() {
    ChefMateTheme {
        CookbookListScreen(
            state = CookbookListState(
                cookbooks = AsyncState.Content(
                    persistentListOf(
                        CookbookUiModel(
                            id = "1",
                            name = UiText.DynamicString("Cookbook 1"),
                            imageUrls = persistentListOf("https://example.com/image1.jpg"),
                            recipeCount = 10
                        ),
                        CookbookUiModel(
                            id = "2",
                            name = UiText.DynamicString("Cookbook 2"),
                            imageUrls = persistentListOf("https://example.com/image2.jpg"),
                            recipeCount = 20
                        )
                    )
                )
            ),
            onAction = { /* no-op */ },
            navigator = CookbookListNavigatorImpl()
        )
    }
}
