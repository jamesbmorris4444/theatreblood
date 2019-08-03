package com.greendot.rewards.home

import com.greendot.rewards.Constants.API_KEY
import com.greendot.rewards.Constants.LANGUAGE
import com.greendot.rewards.logger.LogUtils
import com.greendot.rewards.logger.LogUtils.TAG_FILTER.ANX
import com.greendot.rewards.repository.APIClient
import com.greendot.rewards.repository.APIInterface
import com.greendot.rewards.repository.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeDataModel : Callback<ArrayList<Movie>> {

    private lateinit var movieList: ArrayList<Movie>
    private val moviesService: APIInterface = APIClient.client

    init {
        val callBack: Call<ArrayList<Movie>> = moviesService.getMovies(API_KEY, LANGUAGE, 1)
        callBack.enqueue(this)
    }

    override fun onResponse(call: Call<ArrayList<Movie>>, response: Response<ArrayList<Movie>>) {
        if (response.isSuccessful) {
            response.body()?.let { movieList = it }
        } else {
            println(response.errorBody())
        }
}

    override fun onFailure(call: Call<ArrayList<Movie>>, t: Throwable) {
        t.message?.let {
            LogUtils.E(LogUtils.FilterTags.withTags(ANX), it, t)
        }
    }

    fun getSingleMovie(position: Int): Movie? {
        if (position >= 0 && position < movieList.size) {
            return movieList[position]
        } else {
            return null
        }
    }

}