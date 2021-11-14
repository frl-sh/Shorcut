package sharif.feryal.shortcut.task.data.repository

import sharif.feryal.shortcut.task.core.models.Either
import sharif.feryal.shortcut.task.core.extension.awaitEither
import sharif.feryal.shortcut.task.data.mapper.toComic
import sharif.feryal.shortcut.task.data.network.ComicApiService
import sharif.feryal.shortcut.task.domain.Comic
import sharif.feryal.shortcut.task.domain.repository.ComicRepository

class RemoteComicRepository(private val comicApi: ComicApiService) : ComicRepository {
    override suspend fun getCurrentComic(): Either<Comic> = comicApi.getCurrentComic()
        .awaitEither { comicDto ->
            comicDto.toComic()
        }

    override suspend fun getComicByNum(num: Int): Either<Comic> = comicApi.getComicByNumber(num)
        .awaitEither { comicDto ->
            comicDto.toComic()
        }
}