package com.charr0max.kmmtranslator.voicetotext.presentation

import com.charr0max.kmmtranslator.core.domain.util.toCommonStateFlow
import com.charr0max.kmmtranslator.voicetotext.domain.Voice2TextParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VoiceToTextViewModel(
    private val parser: Voice2TextParser,
    coroutineScope: CoroutineScope? = null,
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(VoiceToTextState())
    val state = _state.combine(parser.state) { state, voiceResult ->
        state.copy(
            spokenText = voiceResult.result,
            recordErrorText = if (state.canRecord) voiceResult.error else "Can't record without permission",
            displayState = when {
                !state.canRecord || voiceResult.error != null -> DisplayState.ERROR
                voiceResult.result.isNotBlank() && !voiceResult.isSpeaking -> {
                    DisplayState.DISPLAY_RESULTS
                }

                voiceResult.isSpeaking -> DisplayState.SPEAKING
                else -> DisplayState.WAITING_TO_TALK
            },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VoiceToTextState())
        .toCommonStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                if (state.value.displayState == DisplayState.SPEAKING) {
                    _state.update {
                        it.copy(
                            powerRatios = it.powerRatios + parser.state.value.powerRatio,
                        )
                    }
                }
                delay(50L)
            }
        }
    }

    fun onEvent(event: VoiceToTextEvent) {
        when (event) {
            VoiceToTextEvent.Close -> Unit
            is VoiceToTextEvent.PermissionResult -> {
                _state.update {
                    it.copy(canRecord = event.isGranted)
                }
            }

            VoiceToTextEvent.Reset -> {
                parser.reset()
                _state.update { VoiceToTextState() }
            }

            is VoiceToTextEvent.ToggleRecording -> {
                toggleListening(event.langCode)
            }
        }
    }

    private fun toggleListening(languageCode: String) {
        _state.update { it.copy(powerRatios = emptyList()) }
        parser.cancel()
        if (state.value.displayState == DisplayState.SPEAKING) {
            parser.stopListening()
        } else {
            parser.startListening(languageCode)
        }
    }
}
