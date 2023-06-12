package com.charr0max.kmmtranslator.translate.data.remote

import io.ktor.client.HttpClient

expect class HttpClientFactory {
    fun create(): HttpClient
}