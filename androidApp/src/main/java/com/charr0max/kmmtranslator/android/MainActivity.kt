package com.charr0max.kmmtranslator.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.charr0max.kmmtranslator.android.core.presentation.Routes
import com.charr0max.kmmtranslator.android.translate.presentation.AndroidTranslateViewModel
import com.charr0max.kmmtranslator.android.translate.presentation.TranslateScreen
import com.charr0max.kmmtranslator.android.voicetotext.presentation.AndroidVoiceToTextViewModel
import com.charr0max.kmmtranslator.android.voicetotext.presentation.VoiceToTextScreen
import com.charr0max.kmmtranslator.translate.presentation.TranslateEvent
import com.charr0max.kmmtranslator.voicetotext.presentation.VoiceToTextEvent
import dagger.hilt.android.AndroidEntryPoint

const val LANG_CODE = "languageCode"
const val VOICE_RESULT = "voiceResult"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TranslatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    TranslateRoot()
                }
            }
        }
    }
}

@Composable
fun TranslateRoot() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.TRANSLATE) {
        composable(route = Routes.TRANSLATE) {
            val viewModel: AndroidTranslateViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState()
            val voiceResult by it
                .savedStateHandle
                .getStateFlow<String?>(VOICE_RESULT, null)
                .collectAsState()
            LaunchedEffect(voiceResult) {
                viewModel.onEvent(TranslateEvent.SubmitVoiceResult(voiceResult))
                it.savedStateHandle[VOICE_RESULT] = null
            }
            TranslateScreen(
                state = state.value,
                onEvent = {
                    when (it) {
                        is TranslateEvent.RecordAudio -> {
                            navController.navigate(
                                Routes.VOICE_TO_TEXT + "/${state.value.fromLanguage.language.langCode}",
                            )
                        }

                        else -> viewModel.onEvent(it)
                    }
                },
            )
        }

        composable(
            route = "${Routes.VOICE_TO_TEXT}/{$LANG_CODE}",
            arguments = listOf(
                navArgument(LANG_CODE) {
                    type = NavType.StringType
                    defaultValue = "en"
                },
            ),
        ) { navBackStackEntry ->
            val languageCode = navBackStackEntry.arguments?.getString(LANG_CODE) ?: "en"
            val viewModel: AndroidVoiceToTextViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()
            VoiceToTextScreen(
                state = state,
                languageCode = languageCode,
                onResult = { spokenText ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        VOICE_RESULT,
                        spokenText,
                    )
                    navController.popBackStack()
                },
                onEvent = { event ->
                    when (event) {
                        VoiceToTextEvent.Close -> {
                            navController.popBackStack()
                        }

                        else -> viewModel.onEvent(event)
                    }
                },
            )
        }
    }
}
