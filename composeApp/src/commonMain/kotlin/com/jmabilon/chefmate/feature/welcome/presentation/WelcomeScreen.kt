package com.jmabilon.chefmate.feature.welcome.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmabilon.chefmate.core.designsystem.theme.ChefMateTheme
import com.jmabilon.chefmate.feature.welcome.presentation.component.HeadlineContainer
import com.jmabilon.chefmate.feature.welcome.presentation.component.WelcomeFooter

@Composable
fun WelcomeRoot(
    navigator: WelcomeNavigator
) {
    WelcomeScreen(navigator = navigator)
}

@Composable
private fun WelcomeScreen(
    navigator: WelcomeNavigator
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        WelcomeScreenContent(
            modifier = Modifier.padding(innerPadding),
            navigator = navigator
        )
    }
}

@Composable
private fun WelcomeScreenContent(
    modifier: Modifier = Modifier,
    navigator: WelcomeNavigator
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp, horizontal = 16.dp)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadlineContainer(
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        WelcomeFooter(
            onGetStartedButtonClick = navigator::navigateToOnBoarding,
            onSignInClick = navigator::navigateToSignIn
        )
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    ChefMateTheme {
        WelcomeScreen(navigator = WelcomeNavigatorImpl())
    }
}
