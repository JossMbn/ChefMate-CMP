package com.jmabilon.chefmate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform