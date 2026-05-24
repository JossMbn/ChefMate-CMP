package com.jmabilon.chefmate.feature.account.presentation

enum class AccountContentView {
    Loading, Content
}

data class AccountState(
    val contentView: AccountContentView = AccountContentView.Loading
)
