package sharif.feryal.shortcut.task.core.models

sealed class DataStatus {
    object None: DataStatus()
    object Loading: DataStatus()
    data class Failed(val error: Throwable): DataStatus()
}