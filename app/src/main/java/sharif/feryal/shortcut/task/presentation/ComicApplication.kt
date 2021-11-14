package sharif.feryal.shortcut.task.presentation

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import sharif.feryal.shortcut.task.data.dataModule
import sharif.feryal.shortcut.task.data.network.networkModule

class ComicApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        setUpKoin()
    }

    private fun setUpKoin() {
        startKoin {
            androidContext(this@ComicApplication)
            modules(
                networkModule,
                dataModule,
                presentationModule
            )
        }
    }
}