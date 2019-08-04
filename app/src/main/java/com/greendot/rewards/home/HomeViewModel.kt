package com.greendot.rewards.home

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.greendot.rewards.repository.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {
    private var homeDataModel = HomeDataModel()
    var title: ObservableField<String>? = ObservableField("")
    var releaseDate: ObservableField<String>? = ObservableField("")
    private var disposable: Disposable? = null

    init {
        disposable = homeDataModel.getMovieList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe{ movieList -> liveDataUpdate(movieList) }
    }

    private fun liveDataUpdate(movieList: ArrayList<Movie>) {
        val movie: Movie? = movieList?.let { it.get(0) }
        title?.set(movie?.let { it.title })
        releaseDate?.set(movie?.let { it.releaseDate })
    }
}