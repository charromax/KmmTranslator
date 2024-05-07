package com.charr0max.kmmtranslator.di

import com.charr0max.kmmtranslator.translate.data.local.FakeHistoryDataSource
import com.charr0max.kmmtranslator.translate.data.remote.FakeTranslateClient
import com.charr0max.kmmtranslator.translate.domain.history.HistoryDataSource
import com.charr0max.kmmtranslator.translate.domain.translate.Translate
import com.charr0max.kmmtranslator.translate.domain.translate.TranslateClient
import com.charr0max.kmmtranslator.voicetotext.data.FakeVoiceToTextParser
import com.charr0max.kmmtranslator.voicetotext.domain.Voice2TextParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideFakeTranslateClient(): TranslateClient = FakeTranslateClient()

    @Provides
    @Singleton
    fun provideFakeHistoryDataSource(): HistoryDataSource = FakeHistoryDataSource()

    @Provides
    @Singleton
    fun provideTranslateUseCase(
        client: TranslateClient,
        dataSource: HistoryDataSource,
    ): Translate = Translate(client, dataSource)

    @Provides
    @Singleton
    fun provideFakeVoiceToTextParser(): Voice2TextParser = FakeVoiceToTextParser()
}
