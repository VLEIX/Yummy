package com.frantun.yummy.other

import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun <T> MutableList<T>.assertStateOrder(vararg states: KClass<out Any>) {
    assertEquals(states.size, size)
    states.forEachIndexed { index, kClass ->
        assertTrue(
            "Expected ${
                kClass.supertypes.first().toString().split(".").last()
            } $index to be ${kClass.simpleName}"
        )
        { kClass.isInstance(this[index]) }
    }
}
