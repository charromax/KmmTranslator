package com.charr0max.kmmtranslator.android.translate.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.charr0max.kmmtranslator.android.R
import com.charr0max.kmmtranslator.android.core.theme.LightBlue
import com.charr0max.kmmtranslator.core.presentation.UiLanguage

@Composable
fun LanguageDropDown(
    language: UiLanguage,
    isOpen: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    onLanguageSelected: (UiLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        DropdownMenu(expanded = isOpen, onDismissRequest = onDismiss) {
            UiLanguage.allLanguages.forEach { thisLanguage ->
                LanguageDropDownItem(
                    language = thisLanguage,
                    onClick = { onLanguageSelected(thisLanguage) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Row(modifier = Modifier
            .clickable { onClick() }
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = language.drawableRes, contentDescription = language.language.langName,
                modifier = Modifier.size(30.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(text = language.language.langName, color = LightBlue)
            Icon(
                imageVector = if (isOpen) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = if (isOpen) {
                    stringResource(R.string.close)
                } else {
                    stringResource(R.string.open)
                },
                tint = LightBlue,
                modifier = Modifier.size(30.dp)
            )
        }
    }

}

