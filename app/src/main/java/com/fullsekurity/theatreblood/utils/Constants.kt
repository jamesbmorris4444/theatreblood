package com.fullsekurity.theatreblood.utils

import com.fullsekurity.theatreblood.repository.storage.Donor

object Constants {

    const val MOVIES_ARRAY_DATA_TAG = "results"
    val DONOR_LIST_CLASS_TYPE = (ArrayList<Donor>()).javaClass
    const val THEATRE_BLOOD_BASE_URL = "https://api.themoviedb.org/3/discover/"
    const val ROOT_FRAGMENT_TAG = "ROOT FRAGMENT"
    const val INPUT_FRAGMENT_TAG = "input"
    const val DONORS_FRAGMENT_TAG = "donors"
    const val DONOR_FRAGMENT_TAG = "donor"
    const val IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/"
    const val SMALL_IMAGE_URL_PREFIX = IMAGE_URL_PREFIX + "w300"
    const val API_KEY_REQUEST_PARAM = "api_key"
    const val LANGUAGE_REQUEST_PARAM = "language"
    const val PAGE_REQUEST_PARAM = "page"
    const val API_KEY = "17c5889b399d3c051099e4098ad83493"
    const val LANGUAGE = "en"
    const val DATA_BASE_NAME = "TheatreBlood.db"
    const val STANDARD_LEFT_AND_RIGHT_MARGIN = 20f
    const val STANDARD_EDIT_TEXT_HEIGHT = 60f
    const val STANDARD_BUTTON_HEIGHT = 50f
    const val EDIT_TEXT_TO_BUTTON_RATIO = 3  // 3:1

    // toolbar

    const val INITIAL_TOOLBAR_TITLE = "Theatre Blood"
    const val TOOLBAR_BACKGROUND_COLOR = "#3a3aff"
    const val TOOLBAR_TEXT_COLOR = "#ffffff"

}