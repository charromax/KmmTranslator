package com.charr0max.kmmtranslator.voicetotext.domain

data class VoiceToTextParserState(
    val result: String = "",
    val error: String? = null,
    val powerRatio: Float = 0f,
    val isSpeaking: Boolean = false,
)
