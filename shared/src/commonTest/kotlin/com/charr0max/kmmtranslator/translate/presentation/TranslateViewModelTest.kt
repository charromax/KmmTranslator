package com.charr0max.kmmtranslator.translate.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.charr0max.kmmtranslator.core.presentation.UiLanguage
import com.charr0max.kmmtranslator.translate.data.local.FakeHistoryDataSource
import com.charr0max.kmmtranslator.translate.data.remote.FakeTranslateClient
import com.charr0max.kmmtranslator.translate.domain.history.HistoryItem
import com.charr0max.kmmtranslator.translate.domain.translate.Translate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class TranslateViewModelTest {
    private lateinit var viewModel: TranslateViewModel
    private val client = FakeTranslateClient()
    private val dataSource = FakeHistoryDataSource()
    val translate = Translate(
        client,
        dataSource,
    )

    @BeforeTest
    fun setup() {
        viewModel = TranslateViewModel(
            translate,
            dataSource,
            CoroutineScope(Dispatchers.Default),
        )
    }

    @Test
    fun `State and history items are properly combined`() = runBlocking {
        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(TranslateState())
            val item = HistoryItem(
                id = 0,
                fromLanguageCode = "en",
                fromText = "from",
                toLanguageCode = "de",
                toText = "to",
            )
            dataSource.insertHistoryItem(item)
            val state = awaitItem()

            val expected = UiHistoryItem(
                id = item.id!!,
                fromText = item.fromText,
                toText = item.toText,
                fromLanguage = UiLanguage.byCode(item.fromLanguageCode),
                toLanguage = UiLanguage.byCode(item.toLanguageCode),
            )
            assertThat(state.history.first()).isEqualTo(expected)
        }
    }

    @Test
    fun `translate success - state properly updated`() = runBlocking {
        viewModel.state.test {
            awaitItem()
            viewModel.onEvent(TranslateEvent.ChangeTranslationText("test"))
            awaitItem()
            viewModel.onEvent(TranslateEvent.Translate)

            val loadingState = awaitItem()
            assertThat(loadingState.isTranslating).isTrue()

            val resultState = awaitItem()
            assertThat(resultState.isTranslating).isFalse()
            assertThat(resultState.toText).isEqualTo(client.translatedText)
        }
    }
}
