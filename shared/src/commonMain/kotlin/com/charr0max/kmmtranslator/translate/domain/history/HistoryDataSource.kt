package com.charr0max.kmmtranslator.translate.domain.history

import com.charr0max.kmmtranslator.core.domain.util.CommonFlow

interface HistoryDataSource {
    fun getHistory(): CommonFlow<List<HistoryItem>>
    suspend fun insertHistoryItem(item: HistoryItem)
}