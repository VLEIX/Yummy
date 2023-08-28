package com.frantun.core.extensions

fun Any.simpleClassName(): String = this::class.java.simpleName
