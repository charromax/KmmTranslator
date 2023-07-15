package com.charr0max.kmmtranslator.di

import com.charr0max.kmmtranslator.database.TranslateDatabase
import com.charr0max.kmmtranslator.translate.data.client.KtorTranslateClient
import com.charr0max.kmmtranslator.translate.data.history.SqlDelightHistoryDataSource
import com.charr0max.kmmtranslator.translate.data.local.DatabaseDriverFactory
import com.charr0max.kmmtranslator.translate.data.remote.HttpClientFactory
import com.charr0max.kmmtranslator.translate.domain.translate.Translate
import com.charr0max.kmmtranslator.translate.domain.translate.TranslateClient

class AppModule {
    val historyDataSource by lazy {
        SqlDelightHistoryDataSource(
            TranslateDatabase(DatabaseDriverFactory().create())
        )
    }

    private val translateClient: TranslateClient by lazy {
        KtorTranslateClient(
            HttpClientFactory().create()
        )
    }

    val translateUseCase: Translate by lazy {
        Translate(translateClient, historyDataSource)
    }
}