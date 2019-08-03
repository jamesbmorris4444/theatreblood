package com.greendot.rewards

import com.greendot.rewards.repository.Movie

object Constants {
    // Network
    val MOVIES_ARRAY_DATA_TAG = "results"
    val MOVIE_ARRAY_LIST_CLASS_TYPE = (ArrayList<Movie>()).javaClass
    val POPULAR_MOVIES_BASE_URL = "https://api.themoviedb.org/3/discover/"
    private val IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/"
    val SMALL_IMAGE_URL_PREFIX = IMAGE_URL_PREFIX + "w300"
    val BIG_IMAGE_URL_PREFIX = IMAGE_URL_PREFIX + "w500"
    const val API_KEY_REQUEST_PARAM = "api_key"
    const val LANGUAGE_REQUEST_PARAM = "language"
    const val PAGE_REQUEST_PARAM = "page"
    val API_KEY = "17c5889b399d3c051099e4098ad83493"
    val LANGUAGE = "en"
    val LOADING_PAGE_SIZE = 20
    // DB
}