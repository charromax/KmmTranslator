package com.charr0max.kmmtranslator.voicetotext.presentation

sealed class VoiceToTextEvent {
    object Close : VoiceToTextEvent()
    data class PermissionResult(
        val isGranted: Boolean,
        val isPermanentlyDeclinedL: Boolean,
    ) : VoiceToTextEvent()

    data class ToggleRecording(val langCode: String) : VoiceToTextEvent()
    object Reset : VoiceToTextEvent()
}
