package com.charr0max.kmmtranslator.android.voicetotext.di

import android.app.Application
import com.charr0max.kmmtranslator.android.voicetotext.data.AndroidVoice2TextParser
import com.charr0max.kmmtranslator.voicetotext.domain.Voice2TextParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object Voice2TextModule {
    @Provides
    @ViewModelScoped
    fun provideVoice2TextParser(app: Application): Voice2TextParser {
        return AndroidVoice2TextParser(app)
    }
}
