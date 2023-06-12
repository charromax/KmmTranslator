package com.charr0max.kmmtranslator.translate.data.local

import android.content.Context
import com.charr0max.kmmtranslator.database.TranslateDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory (
    private val context: Context
) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(TranslateDatabase.Schema, context, "translate.db")
    }
}