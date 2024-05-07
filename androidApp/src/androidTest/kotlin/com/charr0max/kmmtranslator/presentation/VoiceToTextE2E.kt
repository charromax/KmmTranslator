package com.charr0max.kmmtranslator.presentation

import android.Manifest
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import com.charr0max.kmmtranslator.android.MainActivity
import com.charr0max.kmmtranslator.android.R
import com.charr0max.kmmtranslator.android.di.AppModule
import com.charr0max.kmmtranslator.android.voicetotext.di.Voice2TextModule
import com.charr0max.kmmtranslator.translate.data.remote.FakeTranslateClient
import com.charr0max.kmmtranslator.translate.domain.translate.TranslateClient
import com.charr0max.kmmtranslator.voicetotext.data.FakeVoiceToTextParser
import com.charr0max.kmmtranslator.voicetotext.domain.Voice2TextParser
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class, Voice2TextModule::class)
class VoiceToTextE2E {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val permissionRule = GrantPermissionRule.grant(
        Manifest.permission.RECORD_AUDIO,
    )

    @Inject
    lateinit var fakeVoiceParser: Voice2TextParser

    @Inject
    lateinit var fakeClient: TranslateClient

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun recordAndTranslate() = runBlocking<Unit> {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val parser = fakeVoiceParser as FakeVoiceToTextParser
        val client = fakeClient as FakeTranslateClient

        composeRule
            .onNodeWithContentDescription(context.getString(R.string.record_audio))
            .performClick()

        composeRule
            .onNodeWithContentDescription(context.getString(R.string.record_audio))
            .performClick()

        composeRule
            .onNodeWithContentDescription(context.getString(R.string.stop_recording))
            .performClick()

        composeRule
            .onNodeWithText(parser.voiceResult)
            .assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription(context.getString(R.string.apply))
            .performClick()

        composeRule
            .onNodeWithText(parser.voiceResult)
            .assertIsDisplayed()
        composeRule
            .onNodeWithText(
                context.getString(R.string.translate),
                ignoreCase = true,
            )
            .performClick()

        composeRule
            .onNodeWithText(parser.voiceResult)
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(client.translatedText)
            .assertIsDisplayed()
    }
}
