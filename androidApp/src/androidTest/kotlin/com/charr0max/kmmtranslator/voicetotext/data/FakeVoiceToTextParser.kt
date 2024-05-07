package com.charr0max.kmmtranslator.voicetotext.data

import com.charr0max.kmmtranslator.core.domain.util.CommonStateFlow
import com.charr0max.kmmtranslator.core.domain.util.toCommonStateFlow
import com.charr0max.kmmtranslator.voicetotext.domain.Voice2TextParser
import com.charr0max.kmmtranslator.voicetotext.domain.VoiceToTextParserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeVoiceToTextParser: Voice2TextParser {

    private val _state = MutableStateFlow(VoiceToTextParserState())
    var voiceResult = "Test result"
    override val state: CommonStateFlow<VoiceToTextParserState>
        get() = _state.toCommonStateFlow()

    override fun startListening(langCode: String) {
        _state.update {
            it.copy(
                isSpeaking = true,
                result = ""
            )
        }
    }

    override fun stopListening() {
        _state.update {
            it.copy(
                isSpeaking = false,
                result = voiceResult
            )
        }
    }

    override fun cancel()  = Unit

    override fun reset() {
        _state.update {
            VoiceToTextParserState()
        }
    }
}