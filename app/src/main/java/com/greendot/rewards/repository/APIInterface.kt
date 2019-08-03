package com.greendot.rewards.repository

import com.greendot.rewards.Constants.API_KEY_REQUEST_PARAM
import com.greendot.rewards.Constants.LANGUAGE_REQUEST_PARAM
import com.greendot.rewards.Constants.PAGE_REQUEST_PARAM
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface APIInterface {
    @GET("movie")
    fun getMovies(
        @Query(API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(LANGUAGE_REQUEST_PARAM) language: String,
        @Query(PAGE_REQUEST_PARAM) page: Int
    ): Call<ArrayList<Movie>>
}