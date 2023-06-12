package com.charr0max.kmmtranslator.translate.presentation

import com.charr0max.kmmtranslator.core.presentation.UiLanguage

sealed class TranslateEvent {
    data class ChooseFromLanguage(val language: UiLanguage) : TranslateEvent()
    data class ChooseToLanguage(val language: UiLanguage) : TranslateEvent()
    object StopChoosingLanguage : TranslateEvent()
    object SwapLanguages : TranslateEvent()
    data class ChangeTranslationText(val text: String) : TranslateEvent()
    object OpenFromLanguageDropDown : TranslateEvent()
    object OpenToLanguageDropDown : TranslateEvent()
    object CloseTranslation : TranslateEvent()
    data class SelectHistoryItem(val item: UiHistoryItem) : TranslateEvent()
    object EditTranslation : TranslateEvent()
    object RecordAudio : TranslateEvent()
    data class SubmitVoiceResult(val result: String?) : TranslateEvent()
    object OnErrorSeen : TranslateEvent()
    object Translate : TranslateEvent()
}
