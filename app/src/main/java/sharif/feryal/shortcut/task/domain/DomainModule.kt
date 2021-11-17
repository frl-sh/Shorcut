package sharif.feryal.shortcut.task.domain

import org.koin.dsl.module
import sharif.feryal.shortcut.task.domain.interactor.GetComicUseCase

val domainModule = module {

    factory { GetComicUseCase(get(), get()) }
}