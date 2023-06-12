package com.charr0max.kmmtranslator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform