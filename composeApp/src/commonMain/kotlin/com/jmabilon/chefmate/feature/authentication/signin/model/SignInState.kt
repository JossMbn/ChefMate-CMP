package com.jmabilon.chefmate.feature.authentication.signin.model

data class SignInState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)
