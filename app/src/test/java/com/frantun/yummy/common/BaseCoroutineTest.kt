package com.frantun.yummy.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

/**
 * Base class with Coroutines support.
 */
@ExperimentalCoroutinesApi
abstract class BaseCoroutineTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
}
