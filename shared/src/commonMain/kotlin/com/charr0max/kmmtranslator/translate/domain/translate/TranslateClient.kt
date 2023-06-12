package com.charr0max.kmmtranslator.translate.domain.translate

import com.charr0max.kmmtranslator.core.domain.language.Language

interface TranslateClient {
    suspend fun translate(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): String
}