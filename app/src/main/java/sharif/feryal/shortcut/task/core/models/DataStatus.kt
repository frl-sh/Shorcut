package sharif.feryal.shortcut.task.core.models

sealed class LoadableData<out T> {
    object None: LoadableData<Nothing>()
    object Loading: LoadableData<Nothing>()
    data class Loaded<T>(val data: T): LoadableData<T>()
}

data class Failure(val throwable: Throwable, val retry: Retry? = null)
data class Retry(val comicNumber: Int?)
