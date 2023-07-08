package com.frantun.yummy.other

fun String?.whenNullUse(value: String) = this ?: value
