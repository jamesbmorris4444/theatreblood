package com.fullsekurity.theatreblood.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.utils.ContextInjectorModule
import com.fullsekurity.theatreblood.utils.DaggerContextDependencyInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val activity: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(activity) as T
    }
}

@Suppress("UNCHECKED_CAST")
class HomeViewModel(val activity: Application) : AndroidViewModel(activity) {
    private val homeDataModel = HomeDataModel()
    private val liveHomeDataObject: MutableLiveData<HomeDataObject> = MutableLiveData()
    private lateinit var homeDataObject: HomeDataObject
    private var disposable: Disposable? = null
    private val context: Context = getApplication<Application>().applicationContext

    internal var repository: Repository? = null
        @Inject set

    private val _title = MutableLiveData<String>().apply { value = "" } ; val title: LiveData<String> = _title
    private val _releaseDate = MutableLiveData<String>().apply { value = "" } ; val releaseDate: LiveData<String> = _releaseDate
    private val _posterPath = MutableLiveData<String>().apply { value = "" } ; val posterPath: LiveData<String> = _posterPath

    init {
        DaggerContextDependencyInjector.builder()
            .contextInjectorModule(ContextInjectorModule(context))
            .build()
            .inject(this)
        disposable = homeDataModel.getHomeDataObject()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe{
                homeViewData -> liveHomeDataObject.postValue(homeViewData)
                this.homeDataObject = homeViewData
            }
        homeDataModel.loadData()
    }

    override fun onCleared() {
        disposable?.let {
            it.dispose()
            disposable = null
        }
    }

    fun getLiveHomeDataObject(): LiveData<HomeDataObject> {
        return liveHomeDataObject
    }

    fun liveDataUpdate() {
        val ( title, releaseDate, posterPath ) = homeDataObject
        _title.value = title
        _releaseDate.value = releaseDate
        _posterPath.value = posterPath
    }

    fun onItemClick() {
        repository?.getAllDonors()?.let { list ->
            for (donor in list) {
                LogUtils.W("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.DAO), String.format("all donors: %s", donor.title))
            }
        }
    }
}