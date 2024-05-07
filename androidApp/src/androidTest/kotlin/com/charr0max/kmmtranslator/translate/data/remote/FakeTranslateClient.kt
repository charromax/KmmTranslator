package com.charr0max.kmmtranslator.translate.data.remote

import com.charr0max.kmmtranslator.core.domain.language.Language
import com.charr0max.kmmtranslator.translate.domain.translate.TranslateClient

class FakeTranslateClient : TranslateClient {
    var translatedText = "test translation"
    override suspend fun translate(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language,
    ) = translatedText
}
