package com.charr0max.kmmtranslator.translate.data.history

import com.charr0max.kmmtranslator.database.HistoryEntity
import com.charr0max.kmmtranslator.translate.domain.history.HistoryItem

fun HistoryEntity.toHistoryItem(): HistoryItem {
    return HistoryItem(
        id = id,
        fromLanguageCode = fromLanguageCode,
        fromText = fromText,
        toLanguageCode = toLanguageCode,
        toText = toText
    )
}