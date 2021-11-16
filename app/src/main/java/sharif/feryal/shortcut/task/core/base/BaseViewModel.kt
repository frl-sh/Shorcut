package sharif.feryal.shortcut.task.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel(private val coroutineContexts: CoroutineDispatcherProvider) :
    ViewModel() {

    fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(block = block)
    }

    suspend inline fun <T> onBg(crossinline coroutine: suspend () -> T): T {
        return withContext(bgDispatcher()) {
            coroutine()
        }
    }

    suspend inline fun <T> onUI(crossinline coroutine: suspend () -> T): T {
        return withContext(uiDispatcher()) {
            coroutine()
        }
    }

    fun bgDispatcher(): CoroutineDispatcher {
        return coroutineContexts.bgDispatcher()
    }

    fun uiDispatcher(): CoroutineDispatcher {
        return coroutineContexts.uiDispatcher()
    }
}