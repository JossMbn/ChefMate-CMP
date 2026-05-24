package com.jmabilon.chefmate.feature.authentication.signup.presentation

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)
