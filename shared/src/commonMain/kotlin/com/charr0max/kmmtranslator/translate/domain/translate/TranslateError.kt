package com.charr0max.kmmtranslator.translate.domain.translate

enum class TranslateError {
    SERVICE_UNAVAILABLE,
    CLIENT_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR
}

class TranslateException(val error: TranslateError) : Exception(
    "An error ocurred while translating: $error"
)