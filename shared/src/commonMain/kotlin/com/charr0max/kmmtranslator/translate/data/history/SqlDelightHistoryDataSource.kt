package com.charr0max.kmmtranslator.translate.data.history

import com.charr0max.kmmtranslator.core.domain.util.CommonFlow
import com.charr0max.kmmtranslator.core.domain.util.toCommonFlow
import com.charr0max.kmmtranslator.database.TranslateDatabase
import com.charr0max.kmmtranslator.translate.domain.history.HistoryDataSource
import com.charr0max.kmmtranslator.translate.domain.history.HistoryItem
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class SqlDelightHistoryDataSource(
    private val db: TranslateDatabase
) : HistoryDataSource {
    private val queries = db.translateQueries
    override fun getHistory(): CommonFlow<List<HistoryItem>> {
        return queries
            .getHistory()
            .asFlow()
            .mapToList()
            .map { it.map { it.toHistoryItem() } }
            .toCommonFlow()
    }

    override suspend fun insertHistoryItem(item: HistoryItem) {
        queries
            .inserHistoryEntity(
                id = item.id,
                fromLanguageCode = item.fromLanguageCode,
                fromText = item.fromText,
                toLanguageCode = item.toLanguageCode,
                toText = item.toText,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
    }
}