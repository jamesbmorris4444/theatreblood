package com.fullsekurity.theatreblood.home

import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.logger.LogUtils.TagFilter.ANX
import com.fullsekurity.theatreblood.repository.network.api.APIInterface
import com.fullsekurity.theatreblood.repository.network.api.DBAPIClient
import com.fullsekurity.theatreblood.repository.storage.datamodel.Movie
import com.fullsekurity.theatreblood.utils.Constants.API_KEY
import com.fullsekurity.theatreblood.utils.Constants.LANGUAGE
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
    private val moviesService: APIInterface = DBAPIClient.client
    private lateinit var homeViewData: HomeViewData

    fun loadData() {
        val rand = Random(System.currentTimeMillis())
        val page = rand.nextInt(500)
        val callBack: Call<ArrayList<Movie>> = moviesService.getMovies(API_KEY, LANGUAGE, page)
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
        if (movie != null) {
            homeViewData = HomeViewData(movie.title, movie.releaseDate, movie.posterPath)
            homeViewDataObservable.onNext(homeViewData)
        }
    }

    fun getHomeViewData(): Observable<HomeViewData> {
        return homeViewDataObservable
    }

}