package com.jmabilon.chefmate.feature.collection.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_close_rounded_outlined
import com.jmabilon.chefmate.core.presentation.ObserveAsEvent
import com.jmabilon.chefmate.core.presentation.extension.plus
import com.jmabilon.chefmate.designsystem.component.appbar.CMTopAppBar
import com.jmabilon.chefmate.designsystem.component.appbar.TopAppBarBackIcon
import com.jmabilon.chefmate.designsystem.component.button.CMButton
import com.jmabilon.chefmate.designsystem.extension.customClickable
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.collection.selection.model.CollectionSelectionAction
import com.jmabilon.chefmate.feature.collection.selection.model.CollectionSelectionEvent
import com.jmabilon.chefmate.feature.collection.selection.model.CollectionSelectionState
import com.jmabilon.chefmate.feature.collection.selection.model.ui.CollectionSelectionUiModel
import com.jmabilon.chefmate.feature.collection.selection.navigation.CollectionSelectionNavigator
import com.jmabilon.chefmate.feature.collection.selection.navigation.CollectionSelectionNavigatorImpl
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CollectionSelectionRoot(
    viewModel: CollectionSelectionViewModel = koinViewModel(),
    navigator: CollectionSelectionNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.event) { event ->
        when (event) {
            is CollectionSelectionEvent.OnUpdateRecipeCollectionsSuccess -> {
                navigator.navigateBack()
            }
        }
    }

    CollectionSelectionPage(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@Composable
private fun CollectionSelectionPage(
    state: CollectionSelectionState,
    onAction: (CollectionSelectionAction) -> Unit,
    navigator: CollectionSelectionNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CMTopAppBar(
                title = "Collections",
                navigationIcon = {
                    TopAppBarBackIcon(
                        painter = painterResource(Res.drawable.ic_close_rounded_outlined),
                        onClick = navigator::navigateBack
                    )
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CMButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Confirm",
                    onClick = { onAction(CollectionSelectionAction.OnConfirmClick) }
                )
            }
        }
    ) { innerPadding ->
        CollectionSelectionPageContent(
            innerPadding = innerPadding,
            state = state,
            onAction = onAction
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollectionSelectionPageContent(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    state: CollectionSelectionState,
    onAction: (CollectionSelectionAction) -> Unit
) {
    val lazyListState = rememberLazyListState()

    val contentPadding = remember(innerPadding) { innerPadding + PaddingValues(16.dp) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = contentPadding
    ) {
        items(state.collections) { collection ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .customClickable(
                        onClick = {
                            onAction(
                                CollectionSelectionAction.OnCollectionClicked(
                                    collectionId = collection.id
                                )
                            )
                        }
                    )
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = collection.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Checkbox(
                    checked = collection.isSelected,
                    onCheckedChange = {
                        onAction(
                            CollectionSelectionAction.OnCollectionClicked(
                                collectionId = collection.id
                            )
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionSelectionPagePreview() {
    ChefMateTheme {
        CollectionSelectionPage(
            state = CollectionSelectionState(
                collections = persistentListOf(
                    CollectionSelectionUiModel(
                        id = "1",
                        name = "Dinner Recipes",
                        isSelected = true
                    ),
                    CollectionSelectionUiModel(
                        id = "2",
                        name = "Dessert Recipes",
                        isSelected = false
                    ),
                    CollectionSelectionUiModel(
                        id = "3",
                        name = "Healthy Recipes",
                        isSelected = true
                    )
                )
            ),
            onAction = { /* no-op */ },
            navigator = CollectionSelectionNavigatorImpl(null)
        )
    }
}
