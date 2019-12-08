package com.fullsekurity.theatreblood.home

import android.app.Application
import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.ContextInjectorModule
import com.fullsekurity.theatreblood.utils.DaggerContextDependencyInjector
import com.squareup.picasso.Picasso
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

    @Inject
    lateinit var repository: Repository

    var title: ObservableField<String> = ObservableField("")
    var releaseDate: ObservableField<String> = ObservableField("")
    var posterPath: ObservableField<String> = ObservableField("")

    companion object {
        @BindingAdapter("android:src")
        @JvmStatic
        fun setImageUrl(view: ImageView, url: String?) {
            if (url != null) {
                Picasso
                    .get()
                    .load(Constants.SMALL_IMAGE_URL_PREFIX + url)
                    .into(view)
            }
        }
    }

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
        val ( title_, releaseDate_, posterPath_ ) = homeDataObject
        title.set(title_)
        releaseDate.set(releaseDate_)
        posterPath.set(posterPath_)
    }

    fun onItemClick() {
        homeDataModel.storeHomeDataObject()
//        mainActivity.supportFragmentManager.beginTransaction()
//            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
//            .replace(R.id.fragments_container, HomeFragment.newInstance())
//            .addToBackStack(null)
//            .commitAllowingStateLoss()
    }
}