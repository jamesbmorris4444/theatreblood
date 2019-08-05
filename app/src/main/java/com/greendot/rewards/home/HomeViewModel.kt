package com.greendot.rewards.home

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greendot.rewards.Constants
import com.greendot.rewards.activity.MainActivity
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(activity: MainActivity) : ViewModel() {
    private val homeDataModel = HomeDataModel()
    private val liveHomeViewData: MutableLiveData<HomeViewData> = MutableLiveData()
    private lateinit var homeViewData: HomeViewData
    var title: ObservableField<String> = ObservableField("")
    var releaseDate: ObservableField<String> = ObservableField("")
    var posterPath: ObservableField<String> = ObservableField("")
    private var disposable: Disposable? = null
    private val mainActivity: MainActivity = activity

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
        disposable = homeDataModel.getHomeViewData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe{
                homeViewData -> liveHomeViewData.postValue(homeViewData)
                this.homeViewData = homeViewData
            }
        homeDataModel.loadData()
    }

    fun unsubscribe() {
        disposable?.let {
            it.dispose()
            disposable = null
        }
    }

    fun getLiveHomeViewData(): LiveData<HomeViewData> {
        return liveHomeViewData
    }

    fun liveDataUpdate() {
        val ( title_, releaseDate_, posterPath_ ) = homeViewData
        title.set(title_ ?: "NO DATA")
        releaseDate.set(releaseDate_ ?: "NO DATA")
        posterPath.set(posterPath_ ?: "NO DATA")
    }

    fun onItemClick() {
        homeDataModel.storeHomeViewData()
//        mainActivity.supportFragmentManager.beginTransaction()
//            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
//            .replace(R.id.fragments_container, HomeFragment.newInstance())
//            .addToBackStack(null)
//            .commitAllowingStateLoss()
    }
}