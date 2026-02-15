package com.jmabilon.chefmate.feature.authentication.signup

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
import com.jmabilon.chefmate.feature.authentication.signup.model.SignUpAction
import com.jmabilon.chefmate.feature.authentication.signup.model.SignUpState
import com.jmabilon.chefmate.feature.authentication.signup.navigation.SignUpNavigator
import com.jmabilon.chefmate.feature.authentication.signup.navigation.SignUpNavigatorImpl

@Composable
fun SignUpRoot(
    viewModel: SignUpViewModel = viewModel(),
    navigator: SignUpNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignUpPage(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@Composable
private fun SignUpPage(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    navigator: SignUpNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        SignUpPageContent(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }
}

@Composable
private fun SignUpPageContent(
    modifier: Modifier = Modifier,
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    navigator: SignUpNavigator
) {
    // ...
}

@Preview
@Composable
private fun SignUpPagePreview() {
    ChefMateTheme {
        SignUpPage(
            state = SignUpState(),
            onAction = { /* no-op */ },
            navigator = SignUpNavigatorImpl()
        )
    }
}
