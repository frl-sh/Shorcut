package sharif.feryal.shortcut.task.core.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object ComicDispatcherProvider: CoroutineDispatcherProvider {
    override fun bgDispatcher(): CoroutineDispatcher = Dispatchers.Default

    override fun uiDispatcher(): CoroutineDispatcher = Dispatchers.Main
}