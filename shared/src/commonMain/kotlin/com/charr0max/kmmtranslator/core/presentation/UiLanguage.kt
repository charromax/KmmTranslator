package com.charr0max.kmmtranslator.core.presentation

import com.charr0max.kmmtranslator.core.domain.language.Language

expect class UiLanguage {
    val language: Language

    companion object {
        fun byCode(langCode: String): UiLanguage
        val allLanguages: List<UiLanguage>
    }
}