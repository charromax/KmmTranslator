package com.charr0max.kmmtranslator.translate.presentation

import com.charr0max.kmmtranslator.core.presentation.UiLanguage
import com.charr0max.kmmtranslator.translate.domain.translate.TranslateError

data class TranslateState(
    val fromText: String = "",
    val toText: String? = null,
    val isTranslating: Boolean = false,
    val fromLanguage: UiLanguage = UiLanguage.byCode("en"),
    val toLanguage: UiLanguage = UiLanguage.byCode("es"),
    val isChoosingFromLanguage: Boolean= false,
    val isChoosingToLanguage: Boolean= false,
    val error: TranslateError? = null,
    val history: List<UiHistoryItem> = emptyList()
)
