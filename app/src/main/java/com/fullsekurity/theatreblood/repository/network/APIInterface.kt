package com.fullsekurity.theatreblood.repository.network

import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.utils.Constants.API_KEY_REQUEST_PARAM
import com.fullsekurity.theatreblood.utils.Constants.INCLUDE_ADULT_PARAM
import com.fullsekurity.theatreblood.utils.Constants.INCLUDE_VIDEO_PARAM
import com.fullsekurity.theatreblood.utils.Constants.LANGUAGE_REQUEST_PARAM
import com.fullsekurity.theatreblood.utils.Constants.PAGE_REQUEST_PARAM
import com.fullsekurity.theatreblood.utils.Constants.SORT_BY_PARAM
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("movie")
    fun getDonors(
        @Query(API_KEY_REQUEST_PARAM) apiKey: String,
        @Query(LANGUAGE_REQUEST_PARAM) language: String,
        @Query(SORT_BY_PARAM) sortBy: String,
        @Query(INCLUDE_ADULT_PARAM) includeAdult: String,
        @Query(INCLUDE_VIDEO_PARAM) includeVideo: String,
        @Query(PAGE_REQUEST_PARAM) page: Int
    ): Observable<DonorResponse>
}

data class DonorResponse (
    val page: Int,
    val totalResults: Int,
    val totalPages: Int,
    val results: List<Donor>
)