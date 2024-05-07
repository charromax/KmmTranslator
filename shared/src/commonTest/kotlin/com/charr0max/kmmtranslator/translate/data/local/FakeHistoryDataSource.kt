package com.charr0max.kmmtranslator.translate.data.local

import com.charr0max.kmmtranslator.core.domain.util.CommonFlow
import com.charr0max.kmmtranslator.core.domain.util.toCommonFlow
import com.charr0max.kmmtranslator.translate.domain.history.HistoryDataSource
import com.charr0max.kmmtranslator.translate.domain.history.HistoryItem
import kotlinx.coroutines.flow.MutableStateFlow

class FakeHistoryDataSource : HistoryDataSource {
    private val _data = MutableStateFlow<List<HistoryItem>>(emptyList())
    override fun getHistory() = _data.toCommonFlow()

    override suspend fun insertHistoryItem(item: HistoryItem) {
        _data.value += item
    }
}
