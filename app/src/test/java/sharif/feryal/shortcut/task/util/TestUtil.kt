package sharif.feryal.shortcut.task.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import sharif.feryal.shortcut.task.core.base.CoroutineDispatcherProvider

@ExperimentalCoroutinesApi
val testCoroutineDispatcher = TestCoroutineDispatcher()

@ExperimentalCoroutinesApi
object MockCoroutineDispatcherProvider : CoroutineDispatcherProvider {

    override fun bgDispatcher(): CoroutineDispatcher =
        testCoroutineDispatcher

    override fun uiDispatcher(): CoroutineDispatcher =
        testCoroutineDispatcher
}