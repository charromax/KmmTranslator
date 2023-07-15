package com.charr0max.kmmtranslator.android.voicetotext.presentation

import android.Manifest.permission.RECORD_AUDIO
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition.Companion.Center
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.charr0max.kmmtranslator.android.R
import com.charr0max.kmmtranslator.android.core.theme.LightBlue
import com.charr0max.kmmtranslator.android.voicetotext.presentation.components.VoiceRecorderDisplay
import com.charr0max.kmmtranslator.voicetotext.presentation.DisplayState
import com.charr0max.kmmtranslator.voicetotext.presentation.VoiceToTextEvent
import com.charr0max.kmmtranslator.voicetotext.presentation.VoiceToTextState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VoiceToTextScreen(
    state: VoiceToTextState,
    languageCode: String,
    onResult: (String) -> Unit,
    onEvent: (VoiceToTextEvent) -> Unit,
) {
    val context = LocalContext.current
    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            onEvent(
                VoiceToTextEvent.PermissionResult(
                    isGranted = isGranted,
                    !isGranted && !(context as ComponentActivity).shouldShowRequestPermissionRationale(
                        RECORD_AUDIO,
                    ),
                ),
            )
        },
    )
    LaunchedEffect(recordAudioPermissionLauncher) {
        recordAudioPermissionLauncher.launch(RECORD_AUDIO)
    }

    Scaffold(
        floatingActionButtonPosition = Center,
        floatingActionButton = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FloatingActionButton(
                    onClick = {
                        if (state.displayState != DisplayState.DISPLAY_RESULTS) {
                            onEvent(VoiceToTextEvent.ToggleRecording(languageCode))
                        } else {
                            onResult(state.spokenText)
                        }
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(75.dp),
                ) {
                    AnimatedContent(targetState = state.displayState) { targetState: DisplayState? ->
                        when (targetState) {
                            DisplayState.SPEAKING -> {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = stringResource(R.string.stop_recording),
                                    modifier = Modifier.size(50.dp),
                                )
                            }

                            DisplayState.DISPLAY_RESULTS -> {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = stringResource(R.string.apply),
                                    modifier = Modifier.size(50.dp),
                                )
                            }

                            else -> {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.mic),
                                    contentDescription = stringResource(R.string.record_audio),
                                    modifier = Modifier.size(50.dp),
                                )
                            }
                        }
                    }
                }
                if (state.displayState == DisplayState.DISPLAY_RESULTS) {
                    IconButton(onClick = {
                        onEvent(VoiceToTextEvent.ToggleRecording(languageCode))
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = stringResource(R.string.record_again),
                            tint = LightBlue,
                        )
                    }
                }
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = {
                        onEvent(VoiceToTextEvent.Close)
                    },
                    modifier = Modifier.align(Alignment.CenterStart),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(id = R.string.close),
                    )
                }
                if (state.displayState == DisplayState.SPEAKING) {
                    Text(
                        text = stringResource(R.string.listening),
                        color = LightBlue,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 100.dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedContent(targetState = state.displayState) { targetState: DisplayState? ->
                    when (targetState) {
                        DisplayState.WAITING_TO_TALK -> {
                            Text(
                                text = "Click record and start talking",
                                style = MaterialTheme.typography.h2,
                                textAlign = TextAlign.Center,
                            )
                        }

                        DisplayState.SPEAKING -> {
                            VoiceRecorderDisplay(
                                powerRatios = state.powerRatios,
                                modifier = Modifier.fillMaxWidth().height(100.dp),
                            )
                        }

                        DisplayState.DISPLAY_RESULTS -> {
                            Text(
                                text = state.spokenText,
                                style = MaterialTheme.typography.h2,
                                textAlign = TextAlign.Center,
                            )
                        }

                        DisplayState.ERROR -> {
                            Text(
                                text = state.recordErrorText ?: "Unknown Error",
                                style = MaterialTheme.typography.h2,
                                textAlign = TextAlign.Center,
                            )
                        }

                        else -> Unit
                    }
//
                }
            }
        }
    }
}
