package sharif.feryal.shortcut.task.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import sharif.feryal.shortcut.task.core.base.ComicDispatcherProvider
import sharif.feryal.shortcut.task.core.base.CoroutineDispatcherProvider
import sharif.feryal.shortcut.task.presentation.viewmodel.ComicBrowserViewModel

val presentationModule = module {
    factory<CoroutineDispatcherProvider> {
        ComicDispatcherProvider
    }

    viewModel {
        ComicBrowserViewModel(get())
    }
}