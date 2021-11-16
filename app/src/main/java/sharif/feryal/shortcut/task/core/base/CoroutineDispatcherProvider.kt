package sharif.feryal.shortcut.task.core.base

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {
    fun bgDispatcher(): CoroutineDispatcher
    fun uiDispatcher(): CoroutineDispatcher
}