package com.jmabilon.chefmate.feature.authentication.signin

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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chefmate.composeapp.generated.resources.Res
import chefmate.composeapp.generated.resources.ic_apple
import chefmate.composeapp.generated.resources.ic_arrow_back_rounded_outlined
import chefmate.composeapp.generated.resources.ic_google
import com.jmabilon.chefmate.designsystem.component.AppLogo
import com.jmabilon.chefmate.designsystem.component.button.CMButton
import com.jmabilon.chefmate.designsystem.component.textfield.CMTextField
import com.jmabilon.chefmate.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.authentication.component.AuthMethodsDivider
import com.jmabilon.chefmate.feature.authentication.component.AuthProviderCompanyButton
import com.jmabilon.chefmate.feature.authentication.signin.model.SignInAction
import com.jmabilon.chefmate.feature.authentication.signin.model.SignInState
import com.jmabilon.chefmate.feature.authentication.signin.navigation.SignInNavigator
import com.jmabilon.chefmate.feature.authentication.signin.navigation.SignInNavigatorImpl
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInRoot(
    viewModel: SignInViewModel = koinViewModel(),
    navigator: SignInNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignInPage(
        state = state,
        onAction = viewModel::onAction,
        navigator = navigator
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInPage(
    state: SignInState,
    onAction: (SignInAction) -> Unit,
    navigator: SignInNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { AppLogo(withAppName = true) },
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
            text = "Sign In",
            style = MaterialTheme.typography.headlineMedium
        )

        CMTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.email,
            onValueChange = { onAction(SignInAction.OnEmailValueChange(it)) },
            label = "Email",
            hint = "john.doe@gmail.com",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.End,
        ) {
            CMTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.password,
                onValueChange = { onAction(SignInAction.OnPasswordValueChange(it)) },
                label = "Password",
                hint = "Azerty123!",
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            TextButton(onClick = navigator::navigateToForgotPasswordPage) {
                Text(
                    text = "Forgot password?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End
                )
            }
        }

        CMButton(
            modifier = Modifier
                .fillMaxWidth(),
            label = "Sign In",
            isLoading = state.isLoading,
            onClick = { onAction(SignInAction.OnSignInClick) }
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
    }
}

@Preview
@Composable
private fun SignInPagePreview() {
    ChefMateTheme(isDarkMode = false) {
        SignInPage(
            state = SignInState(),
            onAction = { /* no-op */ },
            navigator = SignInNavigatorImpl()
        )
    }
}
