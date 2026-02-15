package com.jmabilon.chefmate.feature.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmabilon.chefmate.designsystem.component.CMButton
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.account.model.AccountAction
import com.jmabilon.chefmate.feature.account.model.AccountState
import com.jmabilon.chefmate.feature.account.navigation.AccountNavigator
import com.jmabilon.chefmate.feature.account.navigation.AccountNavigatorImpl
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountRoot(
    viewModel: AccountViewModel = koinViewModel(),
    navigator: AccountNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AccountPage(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@Composable
private fun AccountPage(
    state: AccountState,
    onAction: (AccountAction) -> Unit,
    navigator: AccountNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        AccountPageContent(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }
}

@Composable
private fun AccountPageContent(
    modifier: Modifier = Modifier,
    state: AccountState,
    onAction: (AccountAction) -> Unit,
    navigator: AccountNavigator
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CMButton(
            label = "Sign Out",
            onClick = { onAction(AccountAction.OnSignOutClick) }
        )
    }
}

@Preview
@Composable
private fun AccountPagePreview() {
    ChefMateTheme {
        AccountPage(
            state = AccountState(),
            onAction = { /* no-op */ },
            navigator = AccountNavigatorImpl()
        )
    }
}
