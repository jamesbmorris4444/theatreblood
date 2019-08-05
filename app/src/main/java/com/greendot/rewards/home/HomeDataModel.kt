package com.greendot.rewards.home

import com.greendot.rewards.Constants.API_KEY
import com.greendot.rewards.Constants.LANGUAGE
import com.greendot.rewards.logger.LogUtils
import com.greendot.rewards.logger.LogUtils.TagFilter.ANX
import com.greendot.rewards.repository.APIClient
import com.greendot.rewards.repository.APIInterface
import com.greendot.rewards.repository.Movie
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

private val TAG = HomeDataModel::class.java.simpleName
class HomeDataModel : Callback<ArrayList<Movie>> {

    private lateinit var movieList: ArrayList<Movie>
    private val homeViewDataObservable: ReplaySubject<HomeViewData> = ReplaySubject.create()
    private val moviesService: APIInterface = APIClient.client
    private lateinit var homeViewData: HomeViewData

    fun loadData() {
        val callBack: Call<ArrayList<Movie>> = moviesService.getMovies(API_KEY, LANGUAGE, 3)
        callBack.enqueue(this)
    }

    override fun onResponse(call: Call<ArrayList<Movie>>, response: Response<ArrayList<Movie>>) {
        if (response.isSuccessful) {
            response.body()?.let {
                movieList = it
                storeHomeViewData()
            }
        } else {
            LogUtils.W(TAG, LogUtils.FilterTags.withTags(ANX), String.format("RetroFit response error: %s", response.errorBody().toString()))
        }
    }

    override fun onFailure(call: Call<ArrayList<Movie>>, t: Throwable) {
        t.message?.let { LogUtils.E(LogUtils.FilterTags.withTags(ANX), it, t) }
    }

    fun storeHomeViewData() {
        val rand = Random(System.currentTimeMillis())
        val movie: Movie? = movieList?.let { it[rand.nextInt(20)] }
        homeViewData = HomeViewData(movie?.title, movie?.releaseDate, movie?.posterPath)
        homeViewDataObservable.onNext(homeViewData)
    }

    fun getHomeViewData(): Observable<HomeViewData> {
        return homeViewDataObservable
    }

}