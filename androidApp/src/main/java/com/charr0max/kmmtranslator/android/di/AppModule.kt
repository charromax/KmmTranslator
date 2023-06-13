package com.charr0max.kmmtranslator.android.di

import android.app.Application
import com.charr0max.kmmtranslator.database.TranslateDatabase
import com.charr0max.kmmtranslator.translate.data.client.KtorTranslateClient
import com.charr0max.kmmtranslator.translate.data.history.SqlDelightHistoryDataSource
import com.charr0max.kmmtranslator.translate.data.local.DatabaseDriverFactory
import com.charr0max.kmmtranslator.translate.data.remote.HttpClientFactory
import com.charr0max.kmmtranslator.translate.domain.history.HistoryDataSource
import com.charr0max.kmmtranslator.translate.domain.translate.Translate
import com.charr0max.kmmtranslator.translate.domain.translate.TranslateClient
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClientFactory().create()
    }

    @Provides
    @Singleton
    fun provideTranslateClient(client: HttpClient): TranslateClient {
        return KtorTranslateClient(client)
    }

    @Provides
    @Singleton
    fun provideDatabaseDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).create()
    }

    @Provides
    @Singleton
    fun provideHistoryDataSource(driver: SqlDriver): HistoryDataSource {
        return SqlDelightHistoryDataSource(TranslateDatabase(driver))
    }

    @Provides
    @Singleton
    fun provideTranslateUseCase(client: TranslateClient, dataSource: HistoryDataSource): Translate {
        return Translate(client, dataSource)
    }
}