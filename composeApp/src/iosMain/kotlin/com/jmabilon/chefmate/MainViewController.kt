package com.jmabilon.chefmate

import androidx.compose.ui.window.ComposeUIViewController
import com.jmabilon.chefmate.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App() }
