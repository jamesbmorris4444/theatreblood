package com.greendot.rewards.home

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greendot.rewards.activity.MainActivity
import com.greendot.rewards.repository.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class HomeViewModel(activity: MainActivity) : ViewModel() {
    private var homeDataModel = HomeDataModel()
    private val liveDataMovieList: MutableLiveData<ArrayList<Movie>> = MutableLiveData()
    var title: ObservableField<String>? = ObservableField("")
    var releaseDate: ObservableField<String>? = ObservableField("")
    var posterPath: ObservableField<String>? = ObservableField("")
    private var disposable: Disposable? = null
    private val mainActivity: MainActivity = activity

    init {
        disposable = homeDataModel.getMovieList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe{ movieList -> liveDataMovieList.postValue(movieList) }
        homeDataModel.loadData()
    }

    fun unsubscribe() {
        disposable?.let { it.dispose() }
        disposable = null
    }

    fun getArrayListMovie(): LiveData<ArrayList<Movie>> {
        return liveDataMovieList
    }

    fun liveDataUpdate() {
        val rand = Random(System.currentTimeMillis())
        val movie: Movie? = liveDataMovieList.value?.let { it[rand.nextInt(20)] }
        title?.set(movie?.let { it.title })
        releaseDate?.set(movie?.let { it.releaseDate })
        posterPath?.set(movie?.let { it.posterPath })
    }

    fun onItemClick() {
        homeDataModel.loadData()
//        mainActivity.supportFragmentManager.beginTransaction()
//            .replace(R.id.fragments_container, HomeFragment.newInstance())
//            .addToBackStack(null)
//            .commitNow()
    }
}