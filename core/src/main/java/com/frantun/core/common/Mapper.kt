package com.frantun.core.common

interface Mapper<I, O> {
    fun map(input: I): O
}
