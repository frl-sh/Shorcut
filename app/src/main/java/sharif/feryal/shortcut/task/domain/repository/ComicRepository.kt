package sharif.feryal.shortcut.task.domain.repository

import sharif.feryal.shortcut.task.core.models.Either
import sharif.feryal.shortcut.task.domain.Comic

interface ComicRepository {
    suspend fun getCurrentComic(): Either<Comic>
    suspend fun getComicByNum(num: Int): Either<Comic>
}