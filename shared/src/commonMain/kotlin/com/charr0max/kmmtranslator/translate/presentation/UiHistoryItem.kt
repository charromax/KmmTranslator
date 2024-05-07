package com.charr0max.kmmtranslator.translate.presentation

import com.charr0max.kmmtranslator.core.presentation.UiLanguage

data class UiHistoryItem(
    val id: Long,
    val fromText: String,
    val toText: String,
    val fromLanguage: UiLanguage,
    val toLanguage: UiLanguage
)