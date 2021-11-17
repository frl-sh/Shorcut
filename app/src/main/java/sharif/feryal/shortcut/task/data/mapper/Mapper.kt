package sharif.feryal.shortcut.task.data.mapper

import sharif.feryal.shortcut.task.data.ComicDto
import sharif.feryal.shortcut.task.domain.Comic
import sharif.feryal.shortcut.task.domain.Date

fun ComicDto.toComic() = Comic(
    number,
    title,
    imageUrl,
    Date(year, month, day),
    alt,
    transcript
)
