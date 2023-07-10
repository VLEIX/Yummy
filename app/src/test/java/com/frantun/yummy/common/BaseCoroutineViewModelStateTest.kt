package com.frantun.yummy.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import org.junit.After
import org.junit.Before
import org.mockito.MockitoAnnotations

/**
 * Base class for ViewModels that use sealed class state object.
 */
@ExperimentalCoroutinesApi
open class BaseCoroutineViewModelStateTest<T> : BaseCoroutineTest() {

    protected val scope = TestScope(mainDispatcherRule.testDispatcher)
    protected var stateList = mutableListOf<T>()

    @Before
    open fun before() {
        MockitoAnnotations.openMocks(this)
    }

    @After
    fun after() {
        stateList.clear()
    }
}
