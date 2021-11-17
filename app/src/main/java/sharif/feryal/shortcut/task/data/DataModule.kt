package sharif.feryal.shortcut.task.data

import org.koin.dsl.module
import retrofit2.Retrofit
import sharif.feryal.shortcut.task.data.network.ComicApiService
import sharif.feryal.shortcut.task.data.repository.RemoteComicRepository
import sharif.feryal.shortcut.task.domain.repository.ComicRepository

val dataModule = module {
    factory {
        get<Retrofit>().create(ComicApiService::class.java)
    }

    factory<ComicRepository> {
        RemoteComicRepository(get())
    }
}