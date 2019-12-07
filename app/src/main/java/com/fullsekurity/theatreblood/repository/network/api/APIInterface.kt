package com.fullsekurity.theatreblood.repository.network.api

import com.fullsekurity.theatreblood.repository.storage.datamodel.Movie
import com.fullsekurity.theatreblood.utils.Constants.API_KEY_REQUEST_PARAM
import com.fullsekurity.theatreblood.utils.Constants.LANGUAGE_REQUEST_PARAM
import com.fullsekurity.theatreblood.utils.Constants.PAGE_REQUEST_PARAM
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