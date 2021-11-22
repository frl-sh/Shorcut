package sharif.feryal.shortcut.task.presentation.models

sealed class ComicRequest {
    data class Next(val nextNumber: Int): ComicRequest()
    data class Previous(val previousNumber: Int): ComicRequest()
}