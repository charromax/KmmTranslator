package com.charr0max.kmmtranslator.translate.presentation

import com.charr0max.kmmtranslator.core.domain.util.Resource
import com.charr0max.kmmtranslator.core.domain.util.toCommonStateFlow
import com.charr0max.kmmtranslator.core.presentation.UiLanguage
import com.charr0max.kmmtranslator.translate.domain.history.HistoryDataSource
import com.charr0max.kmmtranslator.translate.domain.translate.Translate
import com.charr0max.kmmtranslator.translate.domain.translate.TranslateException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

/**
 * Shared ViewModel between IOS and ANDROID
 */
class TranslateViewModel(
    private val translate: Translate,
    private val historyDataSource: HistoryDataSource,
    private val coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private var translationJob: Job? = null

    private val _state = MutableStateFlow(TranslateState())
    val state = combine(
        _state, historyDataSource.getHistory()
    ) { state, history ->
        if (state.history != history) {
            state.copy(history = history.mapNotNull { item ->
                UiHistoryItem(
                    id = item.id ?: return@mapNotNull null,
                    fromText = item.fromText,
                    toText = item.toText,
                    fromLanguage = UiLanguage.byCode(item.fromLanguageCode),
                    toLanguage = UiLanguage.byCode(item.toLanguageCode),
                )
            })
        } else state
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TranslateState())
        .toCommonStateFlow()

    private fun translate(state: TranslateState) {
        if (state.isTranslating || state.fromText.isBlank()) {
            return
        }

        translationJob = viewModelScope.launch {
            _state.update {
                it.copy(isTranslating = true)
            }
            val result = translate.execute(
                fromLanguage = state.fromLanguage.language,
                fromText = state
                    .fromText,
                toLanguage = state.toLanguage.language
            )
            when (result) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isTranslating = false,
                            error = (result.throwable as? TranslateException)?.error
                        )
                    }
                }

                is Resource.Success -> _state.update {
                    it.copy(
                        isTranslating = false,
                        toText = result.data
                    )
                }
            }

        }
    }

    fun onEvent(event: TranslateEvent) {
        when (event) {
            is TranslateEvent.ChangeTranslationText -> {
                _state.update { it.copy(fromText = event.text) }
            }

            is TranslateEvent.ChooseFromLanguage -> {
                _state.update {
                    it.copy(
                        fromLanguage = event.language,
                        isChoosingFromLanguage = false
                    )
                }
            }

            is TranslateEvent.ChooseToLanguage -> {
                val newState = _state.updateAndGet {
                    it.copy(
                        toLanguage = event.language,
                        isChoosingToLanguage = false
                    )
                }
                translate(newState)
            }

            TranslateEvent.CloseTranslation -> {
                _state.update {
                    it.copy(
                        isTranslating = false,
                        fromText = "",
                        toText = null
                    )
                }
            }

            TranslateEvent.EditTranslation -> {
                if (state.value.toText != null) {
                    _state.update {
                        it.copy(
                            toText = null,
                            isTranslating = false
                        )
                    }
                }
            }

            TranslateEvent.OnErrorSeen -> {
                _state.update {
                    it.copy(error = null)
                }
            }

            TranslateEvent.OpenFromLanguageDropDown -> {
                _state.update {
                    it.copy(isChoosingFromLanguage = true)
                }
            }

            TranslateEvent.OpenToLanguageDropDown -> {
                _state.update {
                    it.copy(isChoosingToLanguage = true)
                }
            }

            is TranslateEvent.SelectHistoryItem -> {
                translationJob?.cancel()
                _state.update {
                    it.copy(
                        fromText = event.item.fromText,
                        toText = event.item.toText,
                        isTranslating = false,
                        fromLanguage = event.item.fromLanguage,
                        toLanguage = event.item.toLanguage,
                    )
                }
            }

            TranslateEvent.StopChoosingLanguage -> {
                _state.update {
                    it.copy(
                        isChoosingToLanguage = false,
                        isChoosingFromLanguage = false
                    )
                }
            }

            is TranslateEvent.SubmitVoiceResult -> {
                _state.update {
                    it.copy(
                        fromText = event.result ?: it.fromText,
                        isTranslating = event.result == null,
                        toText = if (event.result != null) null else it.toText,
                    )
                }
            }

            TranslateEvent.SwapLanguages -> {
                _state.update {
                    it.copy(
                        fromText = it.toText ?: "",
                        toText = if (it.toText != null) it.fromText else null,
                        fromLanguage = it.toLanguage,
                        toLanguage = it.fromLanguage
                    )
                }
            }

            TranslateEvent.Translate -> {
                translate(state.value)
            }

            else -> Unit
        }
    }

}