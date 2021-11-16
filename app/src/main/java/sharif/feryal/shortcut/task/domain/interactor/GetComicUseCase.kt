package sharif.feryal.shortcut.task.domain.interactor

import kotlinx.coroutines.withContext
import sharif.feryal.shortcut.task.core.base.CoroutineDispatcherProvider
import sharif.feryal.shortcut.task.core.models.Either
import sharif.feryal.shortcut.task.domain.Comic
import sharif.feryal.shortcut.task.domain.repository.ComicRepository

class GetComicUseCase(
    private val comicRepository: ComicRepository,
    private val dispatcherProvider: CoroutineDispatcherProvider
) {

    suspend fun getComic(number: Int? = null): Either<Comic> {
        return withContext(dispatcherProvider.bgDispatcher()) {
            number?.let {
                comicRepository.getComicByNum(it)
            } ?: run {
                comicRepository.getCurrentComic()
            }
        }
    }

}