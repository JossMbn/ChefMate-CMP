package com.jmabilon.chefmate.feature.cookbook.selection.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_add_rounded_fill
import com.jmabilon.chefmate.core.designsystem.component.button.CMButton
import com.jmabilon.chefmate.core.designsystem.extension.negativePadding
import com.jmabilon.chefmate.core.designsystem.newcomponent.appbar.BottomAppBarContainer
import com.jmabilon.chefmate.core.designsystem.newcomponent.appbar.CMTopAppBar
import com.jmabilon.chefmate.core.designsystem.newcomponent.button.IconButton
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.core.presentation.AsyncState
import com.jmabilon.chefmate.core.presentation.ObserveAsEvent
import com.jmabilon.chefmate.core.presentation.extension.plus
import com.jmabilon.chefmate.feature.cookbook.selection.presentation.component.CookbookSelectionEmptyContent
import com.jmabilon.chefmate.feature.cookbook.selection.presentation.component.CookbookSelectionLoadingContent
import com.jmabilon.chefmate.feature.cookbook.selection.presentation.component.item.CookbookSelectionItem
import com.jmabilon.chefmate.feature.cookbook.selection.presentation.model.CookbookSelectionUiModel
import com.jmabilon.chefmate.feature.cookbook.selection.presentation.preview.CookbookSelectionScreenPreviewProvider
import com.jmabilon.chefmate.feature.overlay.cookbook.create.presentation.CookbookCreationBottomSheet
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CookbookSelectionRoot(
    viewModel: CookbookSelectionViewModel = koinViewModel(),
    navigator: CookbookSelectionNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.event) { event ->
        when (event) {
            is CookbookSelectionEvent.OnUpdateRecipeCookbooksSuccess -> {
                navigator.navigateBack()
            }
        }
    }

    CookbookSelectionScreen(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@Composable
private fun CookbookSelectionScreen(
    state: CookbookSelectionState,
    onAction: (CookbookSelectionAction) -> Unit,
    navigator: CookbookSelectionNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CMTopAppBar(
                title = "Cookbooks",
                onNavigationIconClick = navigator::navigateBack,
                actions = {
                    IconButton(
                        painter = painterResource(Res.drawable.ic_add_rounded_fill),
                        contentDescription = "Add Cookbook",
                        onClick = { onAction(CookbookSelectionAction.OnCookbookSelectionClicked) }
                    )
                }
            )
        },
        bottomBar = {
            if (state.cookbooks.isContent) {
                BottomAppBarContainer {
                    CMButton(
                        modifier = Modifier.fillMaxWidth(),
                        label = "Confirm",
                        onClick = { onAction(CookbookSelectionAction.OnConfirmClicked) }
                    )
                }
            }
        }
    ) { innerPadding ->
        CookbookSelectionScreenContent(
            innerPadding = innerPadding,
            state = state,
            onAction = onAction
        )
    }

    state.dialogState?.let { dialogState ->
        when (dialogState) {
            is CookbookSelectionDialogState.CreateCookbook -> {
                CookbookCreationBottomSheet(
                    onDismissRequest = { onAction(CookbookSelectionAction.OnDismissDialog) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CookbookSelectionScreenContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    state: CookbookSelectionState,
    onAction: (CookbookSelectionAction) -> Unit
) {
    when (state.cookbooks) {
        AsyncState.Loading -> {
            CookbookSelectionLoadingContent(innerPadding = innerPadding)
        }

        AsyncState.Failure -> {
            CookbookSelectionEmptyContent(
                innerPadding = innerPadding,
                onAddCookbookClick = { onAction(CookbookSelectionAction.OnCookbookSelectionClicked) }
            )
        }

        is AsyncState.Content -> {
            CookBookSelectionScreen(
                modifier = modifier,
                cookbooks = state.cookbooks.data,
                innerPadding = innerPadding,
                onAction = onAction
            )
        }
    }
}

@Composable
fun CookBookSelectionScreen(
    modifier: Modifier = Modifier,
    cookbooks: ImmutableList<CookbookSelectionUiModel>,
    innerPadding: PaddingValues,
    onAction: (CookbookSelectionAction) -> Unit
) {
    val lazyListState = rememberLazyListState()

    val contentPadding = remember(innerPadding) {
        innerPadding + PaddingValues(horizontal = 22.dp)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = contentPadding
    ) {
        items(cookbooks, key = { it.id }) { cookbook ->
            CookbookSelectionItem(
                modifier = Modifier
                    .negativePadding(horizontal = 22.dp),
                imageUrl = cookbook.imageUrl,
                cookbookName = cookbook.name,
                recipeCount = cookbook.recipeCount,
                checked = cookbook.checked,
                onCheckedChange = {
                    onAction(CookbookSelectionAction.OnCookbookClicked(cookbookId = cookbook.id))
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CookbookSelectionScreenPreview(
    @PreviewParameter(CookbookSelectionScreenPreviewProvider::class) state: CookbookSelectionState
) {
    ChefMateTheme {
        CookbookSelectionScreen(
            state = state,
            onAction = { /* no-op */ },
            navigator = CookbookSelectionNavigatorImpl(null)
        )
    }
}
