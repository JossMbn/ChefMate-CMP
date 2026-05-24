package com.jmabilon.chefmate.feature.authentication.signin.presentation

sealed interface SignInAction {
    data class OnEmailValueChange(val newEmailValue: String) : SignInAction
    data class OnPasswordValueChange(val newPasswordValue: String) : SignInAction
    data object OnSignInClick : SignInAction
}
