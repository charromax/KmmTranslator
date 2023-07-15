package com.charr0max.kmmtranslator.voicetotext.domain

import com.charr0max.kmmtranslator.core.domain.util.CommonStateFlow

interface Voice2TextParser {
    val state: CommonStateFlow<VoiceToTextParserState>
    fun startListening(langCode: String)
    fun stopListening()
    fun cancel()
    fun reset()
}
