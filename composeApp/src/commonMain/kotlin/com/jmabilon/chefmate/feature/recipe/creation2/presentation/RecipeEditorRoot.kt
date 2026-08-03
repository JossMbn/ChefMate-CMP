package com.jmabilon.chefmate.feature.recipe.creation2.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_check_rounded_fill
import com.jmabilon.chefmate.core.designsystem.component.textfield.CMTextField
import com.jmabilon.chefmate.core.designsystem.newcomponent.appbar.CMTopAppBar
import com.jmabilon.chefmate.core.designsystem.newcomponent.button.IconButton
import com.jmabilon.chefmate.core.designsystem.provider.rememberImagePicker
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.component.BaseInfoSection
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.component.difficulty.DifficultySection
import com.jmabilon.chefmate.feature.recipe.creation2.presentation.component.picker.image.RecipeImagePicker
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RecipeEditorRoot(
    viewModel: RecipeEditorViewModel = koinViewModel(),
    navigator: RecipeEditorNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RecipeEditorScreen(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@Composable
private fun RecipeEditorScreen(
    state: RecipeEditorState,
    onAction: (RecipeEditorAction) -> Unit,
    navigator: RecipeEditorNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CMTopAppBar(
                title = "New Recipe",
                onNavigationIconClick = navigator::navigateBack,
                actions = {
                    IconButton(
                        painter = painterResource(Res.drawable.ic_check_rounded_fill),
                        contentDescription = "Save recipe",
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        onClick = navigator::navigateBack
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        RecipeEditorScreenContent(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }
}

@Composable
private fun RecipeEditorScreenContent(
    modifier: Modifier = Modifier,
    state: RecipeEditorState,
    onAction: (RecipeEditorAction) -> Unit,
    navigator: RecipeEditorNavigator
) {
    val imagePicker = rememberImagePicker(
        onImagePicked = { newImage ->
            onAction(RecipeEditorAction.OnImagePicked(newImage))
        }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            RecipeImagePicker(
                image = state.recipe.image,
                onClick = imagePicker::pickImage
            )
        }

        item {
            CMTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.recipe.title,
                onValueChange = { newTitle ->
                    onAction(RecipeEditorAction.OnTitleChanged(newTitle))
                },
                label = "Name",
                hint = "Creamy Tomato Rigatoni"
            )
        }

        item {
            BaseInfoSection(
                prepTime = state.recipe.time.prepTime,
                cookTime = state.recipe.time.cookTime,
                serves = state.recipe.serves,
                onPrepTimeValueChange = { hour, min ->
                    onAction(RecipeEditorAction.OnPrepTimeChanged(hour, min))
                },
                onCookTimeValueChange = { hour, min ->
                    onAction(RecipeEditorAction.OnCookTimeChanged(hour, min))
                },
                onDecreaseServesClick = {
                    onAction(RecipeEditorAction.OnDecreaseServesClicked)
                },
                onIncreaseServesClick = {
                    onAction(RecipeEditorAction.OnIncreaseServesClicked)
                }
            )
        }

        item {
            DifficultySection(
                difficulty = state.recipe.difficulty,
                onClick = { newDifficulty ->
                    onAction(RecipeEditorAction.OnDifficultyChanged(newDifficulty))
                }
            )
        }
    }
}

@Preview
@Composable
private fun RecipeEditorScreenPreview() {
    ChefMateTheme {
        RecipeEditorScreen(
            state = RecipeEditorState(),
            onAction = { /* no-op */ },
            navigator = RecipeEditorNavigatorImpl()
        )
    }
}
