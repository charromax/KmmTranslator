package com.charr0max.kmmtranslator.translate.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TranslateDto(
    @SerialName("q") val textToTranslate: String,
    @SerialName("source") val sourceLanguageCode: String,
    @SerialName("target") val targetLanguageCode: String
)