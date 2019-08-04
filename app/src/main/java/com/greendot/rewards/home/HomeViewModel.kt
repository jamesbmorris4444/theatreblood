package com.greendot.rewards.home

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.greendot.rewards.activity.MainActivity
import com.greendot.rewards.logger.LogUtils
import com.greendot.rewards.repository.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(activity: MainActivity) : ViewModel() {
    private var homeDataModel = HomeDataModel()
    var title: ObservableField<String>? = ObservableField("")
    var releaseDate: ObservableField<String>? = ObservableField("")
    var posterPath: ObservableField<String>? = ObservableField("")
    private var disposable: Disposable? = null
    private val mainActivity: MainActivity = activity

    init {
        disposable = homeDataModel.getMovieList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe{ movieList -> liveDataUpdate(movieList) }
    }

    fun unsubscribe() {
        disposable ?: return
        disposable = null
    }

    private fun liveDataUpdate(movieList: ArrayList<Movie>) {
        val movie: Movie? = movieList?.let { it.get(0) }
        title?.set(movie?.let { it.title })
        releaseDate?.set(movie?.let { it.releaseDate })
        posterPath?.set(movie?.let { it.posterPath })
    }

    fun onItemClick() {
        LogUtils.W("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX aaaaa"))
//        mainActivity.supportFragmentManager.beginTransaction()
//            .replace(R.id.fragments_container, HomeFragment.newInstance())
//            .addToBackStack(null)
//            .commitNow()
    }
}