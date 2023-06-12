package com.charr0max.kmmtranslator.core.domain.util

import kotlinx.coroutines.flow.Flow

actual open class CommonFlow<T> actual constructor(
    private val flow: Flow<T>
) : Flow<T> by flow