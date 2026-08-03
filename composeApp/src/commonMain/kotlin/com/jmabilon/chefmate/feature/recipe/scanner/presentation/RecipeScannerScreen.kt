package com.jmabilon.chefmate.feature.recipe.scanner.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_close_rounded_outlined
import com.jmabilon.chefmate.core.designsystem.component.appbar.CMTopAppBar
import com.jmabilon.chefmate.core.designsystem.component.appbar.TopAppBarBackIcon
import com.jmabilon.chefmate.core.designsystem.provider.rememberImagePicker
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RecipeScannerRoot(
    viewModel: RecipeScannerViewModel = koinViewModel(),
    navigator: RecipeScannerNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RecipeScannerScreen(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@Composable
private fun RecipeScannerScreen(
    state: RecipeScannerState,
    onAction: (RecipeScannerAction) -> Unit,
    navigator: RecipeScannerNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CMTopAppBar(
                actions = {
                    TopAppBarBackIcon(
                        painter = painterResource(Res.drawable.ic_close_rounded_outlined),
                        onClick = { navigator.navigateBack() }
                    )
                }
            )
        }
    ) { innerPadding ->
        when (state.contentView) {
            RecipeScannerContentView.Initializing -> {
                RecipeScannerInitializingScreenContent(
                    state = state,
                    onAction = onAction
                )
            }

            RecipeScannerContentView.Scanning -> {
                RecipeScannerScanningScreenContent(
                    modifier = Modifier.padding(innerPadding),
                    state = state,
                    onAction = onAction,
                    navigator = navigator
                )
            }
        }
    }
}

@Composable
fun RecipeScannerInitializingScreenContent(
    state: RecipeScannerState,
    onAction: (RecipeScannerAction) -> Unit,
) {
    val imagePicker = rememberImagePicker(
        onImagePicked = { newImage ->
            //onAction(RecipeScannerAction.OnImagePick(newImage))
        }
    )

    LaunchedEffect(state.scanningType) {
        when (state.scanningType) {
            RecipeScannerType.ImageScan -> imagePicker.pickImage()

            null -> Unit
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun RecipeScannerScanningScreenContent(
    modifier: Modifier = Modifier,
    state: RecipeScannerState,
    onAction: (RecipeScannerAction) -> Unit,
    navigator: RecipeScannerNavigator
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Analyzing recipe...",
            style = MaterialTheme.typography.headlineSmall
        )

        LoadingIndicator(modifier = Modifier.size(60.dp))
    }
}

@Preview
@Composable
private fun RecipeScannerScreenPreview() {
    ChefMateTheme {
        RecipeScannerScreen(
            state = RecipeScannerState(
                contentView = RecipeScannerContentView.Scanning,
                scanningType = RecipeScannerType.ImageScan
            ),
            onAction = { /* no-op */ },
            navigator = RecipeScannerNavigatorImpl()
        )
    }
}
