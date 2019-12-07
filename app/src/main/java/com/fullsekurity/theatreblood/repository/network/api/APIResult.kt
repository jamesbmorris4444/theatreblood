package com.fullsekurity.theatreblood.repository.network.api

import com.fullsekurity.theatreblood.repository.storage.datamodel.Movie
import com.google.gson.annotations.SerializedName

class APIResult(
    @field:SerializedName("page")
    var page: Int?,
    @field:SerializedName("total_results")
    var totalResults: Int?,
    @field:SerializedName("total_pages")
    var totalPages: Int?,
    @field:SerializedName("results")
    var results: List<Movie> = ArrayList()
)