package com.jmabilon.chefmate.feature.authentication.signup.presentation

sealed interface SignUpAction {
    data class OnEmailValueChange(val newEmailValue: String) : SignUpAction
    data class OnPasswordValueChange(val newPasswordValue: String) : SignUpAction
    data object OnSignUpClick : SignUpAction
}
