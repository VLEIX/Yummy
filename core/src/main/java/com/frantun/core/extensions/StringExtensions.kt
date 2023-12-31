package com.frantun.core.extensions

import androidx.core.text.HtmlCompat

fun String?.whenNullUse(value: String) = this ?: value

fun String?.toHtml() = HtmlCompat.fromHtml(this.orEmpty(), HtmlCompat.FROM_HTML_MODE_LEGACY)
