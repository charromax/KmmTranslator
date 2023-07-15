package com.charr0max.kmmtranslator.android.voicetotext.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charr0max.kmmtranslator.voicetotext.domain.Voice2TextParser
import com.charr0max.kmmtranslator.voicetotext.presentation.VoiceToTextEvent
import com.charr0max.kmmtranslator.voicetotext.presentation.VoiceToTextViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidVoiceToTextViewModel @Inject constructor(
    private val parser: Voice2TextParser,
) : ViewModel() {
    private val viewModel by lazy {
        VoiceToTextViewModel(
            parser = parser,
            coroutineScope = viewModelScope,
        )
    }

    fun onEvent(event: VoiceToTextEvent) = viewModel.onEvent(event)

    val state = viewModel.state
}
