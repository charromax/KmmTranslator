package com.charr0max.kmmtranslator.core.presentation

import com.charr0max.kmmtranslator.core.domain.language.Language

actual class UiLanguage(
    actual val language: Language, val imageName: String
) {
    actual companion object {
        actual fun byCode(langCode: String): UiLanguage {
            return allLanguages.find { it.language.langCode == langCode }
                ?: throw IllegalArgumentException("Invalid or unsupported language code")
        }

        actual val allLanguages: List<UiLanguage>
            get() = Language.values().map {
                UiLanguage(
                    language = it,
                    imageName = it.langName.lowercase()
                )
            }

    }

}