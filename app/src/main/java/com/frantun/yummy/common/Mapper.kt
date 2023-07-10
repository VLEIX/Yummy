package com.frantun.yummy.common

interface Mapper<I, O> {
    fun map(input: I): O
}
