package com.jmabilon.chefmate.feature.authentication.signin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.authentication.signin.model.SignInAction
import com.jmabilon.chefmate.feature.authentication.signin.model.SignInState
import com.jmabilon.chefmate.feature.authentication.signin.navigation.SignInNavigator
import com.jmabilon.chefmate.feature.authentication.signin.navigation.SignInNavigatorImpl

@Composable
fun SignInRoot(
    viewModel: SignInViewModel = viewModel(),
    navigator: SignInNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignInPage(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@Composable
private fun SignInPage(
    state: SignInState,
    onAction: (SignInAction) -> Unit,
    navigator: SignInNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        SignInPageContent(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }
}

@Composable
private fun SignInPageContent(
    modifier: Modifier = Modifier,
    state: SignInState,
    onAction: (SignInAction) -> Unit,
    navigator: SignInNavigator
) {
    // ...
}

@Preview
@Composable
private fun SignInPagePreview() {
    ChefMateTheme {
        SignInPage(
            state = SignInState(),
            onAction = { /* no-op */ },
            navigator = SignInNavigatorImpl()
        )
    }
}
