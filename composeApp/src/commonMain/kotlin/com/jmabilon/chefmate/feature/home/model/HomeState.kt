package com.jmabilon.chefmate.feature.home.model

enum class HomeContentView {
    Loading, Content
}

data class HomeState(
    val contentView: HomeContentView = HomeContentView.Loading
)
