package com.fullsekurity.theatreblood.donors

data class DonorsDataObject (
    val voteCount: Int,
    val video: Boolean,
    val voteAverage: Float,
    val title: String,
    val popularity: Float,
    val posterPath: String,
    val originalLanguage: String,
    val originalTitle: String,
    val backdropPath: String,
    val adult: Boolean,
    val overview: String,
    val releaseDate: String)