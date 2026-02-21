package com.jmabilon.chefmate.feature.account.model

sealed interface AccountAction {
    data object OnSignOutClick : AccountAction
}
