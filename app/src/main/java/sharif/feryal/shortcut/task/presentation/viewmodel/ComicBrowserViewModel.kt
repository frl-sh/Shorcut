package sharif.feryal.shortcut.task.presentation.viewmodel

import sharif.feryal.shortcut.task.core.base.BaseViewModel
import sharif.feryal.shortcut.task.core.base.CoroutineDispatcherProvider
import sharif.feryal.shortcut.task.domain.repository.ComicRepository

class ComicBrowserViewModel(
    private val comicRepository: ComicRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : BaseViewModel(coroutineDispatcherProvider) {


}