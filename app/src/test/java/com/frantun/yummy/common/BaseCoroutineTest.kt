package com.frantun.yummy.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import org.junit.Rule

/**
 * Base class with Coroutines support.
 */
@ExperimentalCoroutinesApi
abstract class BaseCoroutineTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    protected val scope = TestScope(mainDispatcherRule.testDispatcher)
}
