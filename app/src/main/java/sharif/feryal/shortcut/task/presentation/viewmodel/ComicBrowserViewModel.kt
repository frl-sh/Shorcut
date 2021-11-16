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

    private var comicsMaxNumber: Int = 0

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
                    comicsMaxNumber = comic.number
                    _comic.value = comic
                    _dataStatus.value = DataStatus.None
                },
                onFailure = {
                    _dataStatus.value = DataStatus.Failed(it)
                })
        }
    }
}