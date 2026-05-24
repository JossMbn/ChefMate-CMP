package com.jmabilon.chefmate.feature.account.presentation

sealed interface AccountAction {
    data object OnSignOutClick : AccountAction
}
