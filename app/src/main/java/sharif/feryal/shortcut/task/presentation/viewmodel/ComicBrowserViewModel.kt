package sharif.feryal.shortcut.task.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sharif.feryal.shortcut.task.core.base.BaseViewModel
import sharif.feryal.shortcut.task.core.models.DataStatus
import sharif.feryal.shortcut.task.core.models.SingleLiveEvent
import sharif.feryal.shortcut.task.core.models.fold
import sharif.feryal.shortcut.task.domain.Comic
import sharif.feryal.shortcut.task.domain.interactor.GetComicUseCase

class ComicBrowserViewModel(
    private val getComicUseCase: GetComicUseCase
) : BaseViewModel() {

    private var maxComicNumber: Int = Int.MAX_VALUE

    private val _comic = MutableLiveData<Comic>()
    val comic: LiveData<Comic>
        get() = _comic

    private val _dataStatus = SingleLiveEvent<DataStatus>()
    val dataStatus: LiveData<DataStatus>
        get() = _dataStatus

    init {
        fetchComic()
    }

    private fun fetchComic(number: Int? = null) {
        launch {
            _dataStatus.value = DataStatus.Loading
            getComicUseCase.getComic(number).fold(
                onSuccess = { comic ->
                    setMaxNumberIfNeeded(number, comic)
                    _comic.value = comic
                    _dataStatus.value = DataStatus.None
                },
                onFailure = {
                    _dataStatus.value = DataStatus.Failed(it)
                })
        }
    }

    private fun setMaxNumberIfNeeded(number: Int?, comic: Comic) {
        // maxComicNumber will set just on first call which fetch currentComic
        if (number == null) {
            maxComicNumber = comic.number
        }
    }

    fun previousComicRequested() {
        val previousComicNumber = (_comic.value?.number ?: MIN_COMIC_NUMBER) - 1
        if (previousComicNumber <= MIN_COMIC_NUMBER) {
            return
        }
        fetchComic(previousComicNumber)
    }

    fun nextComicRequested() {
        val nextComicNumber = (_comic.value?.number ?: maxComicNumber) + 1
        if (nextComicNumber >= maxComicNumber) {
            return
        }
        fetchComic(nextComicNumber)
    }

    private companion object {
        const val MIN_COMIC_NUMBER = 0
    }
}