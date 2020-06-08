package task.who.coviddashboard

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import io.reactivex.plugins.RxJavaPlugins
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import task.who.coviddashboard.api.ApiServiceProvider
import task.who.coviddashboard.common.ViewModelFactory
import task.who.coviddashboard.common.extension.bindViewModel
import task.who.coviddashboard.data.repository.StatsRepository
import task.who.coviddashboard.ui.viewmodel.MainViewModel
import timber.log.Timber


class WhoApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein {
        import(androidXModule(this@WhoApplication))

        bind() from singleton { ApiServiceProvider.getService() }
        bind() from singleton { StatsRepository(instance()) }
        bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(dkodein) }
        bindViewModel<MainViewModel>() with provider { MainViewModel(instance()) }

    }

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { }
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}