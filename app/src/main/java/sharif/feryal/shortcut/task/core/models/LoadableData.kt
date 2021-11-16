package sharif.feryal.shortcut.task.core.models

sealed class LoadableData<out T> {
    object NotLoaded: LoadableData<Nothing>()
    data class Loading(val isPaginated: Boolean = false): LoadableData<Nothing>()
    data class Loaded<T>(val data: T): LoadableData<T>()
    data class Failed<T>(val data: T? = null, val errorMessages: String?): LoadableData<T>()
}