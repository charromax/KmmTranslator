package com.charr0max.kmmtranslator.voicetotext.presentation

data class VoiceToTextState(
    val powerRatios: List<Float> = listOf(),
    val spokenText: String = "",
    val canRecord: Boolean = false,
    val recordErrorText: String? = null,
    val displayState: DisplayState? = null,
)

enum class DisplayState {
    WAITING_TO_TALK,
    SPEAKING,
    DISPLAY_RESULTS,
    ERROR,
}
