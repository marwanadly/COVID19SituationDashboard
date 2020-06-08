package task.who.coviddashboard.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import task.who.coviddashboard.data.repository.StatsRepository
import timber.log.Timber

class MainViewModel(private val statsRepository: StatsRepository) : ViewModel() {

    private val _mainViewState = MutableLiveData<MainViewState>()
    val mainViewState: LiveData<MainViewState>
        get() = _mainViewState

    private var disposable: Disposable? = null

    fun getStatsByCountry(countryCode: String) {
        disposable = statsRepository.getStatsByCountry(countryCode)
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.features.isNotEmpty()) {
                    val attribute = it.features[0].attribute
                    attribute.countryCode = countryCode
                    _mainViewState.postValue(MainViewState(dataState = attribute))
                }
            }, {
                Timber.e(it)
                _mainViewState.postValue(MainViewState(error = it))
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

}