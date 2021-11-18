package sharif.feryal.shortcut.task.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sharif.feryal.shortcut.task.core.base.BaseViewModel
import sharif.feryal.shortcut.task.core.models.*
import sharif.feryal.shortcut.task.domain.Comic
import sharif.feryal.shortcut.task.domain.interactor.GetComicUseCase

class ComicBrowserViewModel(
    private val getComicUseCase: GetComicUseCase
) : BaseViewModel() {

    private var maxComicNumber: Int = Int.MAX_VALUE
    private var currentComicNumber: Int? = null

    private val _dataStatus = MutableLiveData<LoadableData<Comic>>(LoadableData.None)
    val dataStatus: LiveData<LoadableData<Comic>>
        get() = _dataStatus

    private val _isNextEnabled = MutableLiveData(false)
    val isNextEnabled: LiveData<Boolean>
        get() = _isNextEnabled

    private val _isPreviousEnabled = MutableLiveData(false)
    val isPreviousEnabled: LiveData<Boolean>
        get() = _isPreviousEnabled

    private val _failure = SingleLiveEvent<Failure>()
    val failure: LiveData<Failure>
        get() = _failure

    init {
        fetchComic()
    }

    fun retryRequested(number: Int?) {
        fetchComic(number)
    }

    private fun fetchComic(number: Int? = null) {
        launch {
            _dataStatus.value = LoadableData.Loading
            getComicUseCase.getComic(number).fold(
                onSuccess = { comic ->
                    currentComicNumber = comic.number
                    setMaxNumberIfNeeded(number, comic)
                    _dataStatus.value = LoadableData.Loaded(comic)
                    updateNextAndPreviousAvailability(comic)
                },
                onFailure = {
                    _dataStatus.value = LoadableData.None
                    _failure.value = Failure(it, Retry(number))
                })
        }
    }

    private fun updateNextAndPreviousAvailability(comic: Comic) {
        _isNextEnabled.value = comic.number < maxComicNumber
        _isPreviousEnabled.value = comic.number > MIN_COMIC_NUMBER
    }

    private fun setMaxNumberIfNeeded(number: Int?, comic: Comic) {
        // maxComicNumber will set just on first call which fetch currentComic
        if (number == null) {
            maxComicNumber = comic.number
        }
    }

    fun previousComicRequested() {
        val previousComicNumber = (currentComicNumber ?: MIN_COMIC_NUMBER) - 1
        if (isRequestValid(ComicRequest.Previous(previousComicNumber)).not()) {
            return
        }
        fetchComic(previousComicNumber)
    }

    fun nextComicRequested() {
        val nextComicNumber = (currentComicNumber ?: maxComicNumber) + 1
        if (isRequestValid(ComicRequest.Next(nextComicNumber)).not()) {
            return
        }
        fetchComic(nextComicNumber)
    }

    private fun isRequestValid(request: ComicRequest): Boolean {
        if (currentComicNumber == null) {
            _failure.value = Failure(
                Throwable("Invalid request. You should see the current one first")
            )
            return false
        }
        if (dataStatus.value is LoadableData.Loading) {
            return false
        }
        (request as? ComicRequest.Next)?.apply {
            if (nextNumber > maxComicNumber) {
                _failure.value = Failure(Throwable("There is no next comic"))
                return false
            }
        }
        (request as? ComicRequest.Previous)?.apply {
            if (previousNumber < MIN_COMIC_NUMBER) {
                _failure.value = Failure(Throwable("There is no previous comic"))
                return false
            }
        }
        return true
    }

    private companion object {
        const val MIN_COMIC_NUMBER = 0
    }
}