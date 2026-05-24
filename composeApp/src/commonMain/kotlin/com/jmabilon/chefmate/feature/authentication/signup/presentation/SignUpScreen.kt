package com.jmabilon.chefmate.feature.authentication.signup.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_apple
import chefmate.composeapp.generated.resources.ic_arrow_back_rounded_outlined
import chefmate.composeapp.generated.resources.ic_google
import com.jmabilon.chefmate.core.designsystem.component.AppLogo
import com.jmabilon.chefmate.core.designsystem.component.button.CMButton
import com.jmabilon.chefmate.core.designsystem.component.textfield.CMTextField
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.authentication.component.AuthMethodsDivider
import com.jmabilon.chefmate.feature.authentication.component.AuthProviderCompanyButton
import com.jmabilon.chefmate.feature.authentication.signup.presentation.component.SignInSwitch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpRoot(
    viewModel: SignUpViewModel = koinViewModel(),
    navigator: SignUpNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignUpScreen(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreen(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    navigator: SignUpNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AppLogo(
                        withAppName = true
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigator::navigateBack) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_back_rounded_outlined),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        SignUpScreenContent(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }
}

@Composable
private fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    navigator: SignUpNavigator
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth(),
            text = "Sign Up",
            style = MaterialTheme.typography.headlineMedium
        )

        CMTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.email,
            onValueChange = { onAction(SignUpAction.OnEmailValueChange(it)) },
            label = "Email",
            hint = "john.doe@gmail.com",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        CMTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.password,
            onValueChange = { onAction(SignUpAction.OnPasswordValueChange(it)) },
            label = "Password",
            hint = "Azerty123!",
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        CMButton(
            modifier = Modifier
                .fillMaxWidth(),
            label = "Create Account",
            isLoading = state.isLoading,
            onClick = { onAction(SignUpAction.OnSignUpClick) }
        )

        AuthMethodsDivider(modifier = Modifier.padding(vertical = 20.dp))

        AuthProviderCompanyButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Connect with Apple",
            leadingIcon = painterResource(Res.drawable.ic_apple),
            onClick = { /* no-op */ }
        )

        AuthProviderCompanyButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Connect with Google",
            leadingIcon = painterResource(Res.drawable.ic_google),
            onClick = { /* no-op */ }
        )

        SignInSwitch(
            modifier = Modifier.padding(top = 20.dp),
            onClick = navigator::navigateToSignIn
        )
    }
}

@Preview
@Composable
private fun SignUpScreenPreview() {
    ChefMateTheme {
        SignUpScreen(
            state = SignUpState(),
            onAction = { /* no-op */ },
            navigator = SignUpNavigatorImpl()
        )
    }
}
