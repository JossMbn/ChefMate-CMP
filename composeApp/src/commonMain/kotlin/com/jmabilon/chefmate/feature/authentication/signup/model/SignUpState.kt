package com.jmabilon.chefmate.feature.authentication.signup.model

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)
