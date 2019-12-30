package com.fullsekurity.theatreblood.utils

import com.fullsekurity.theatreblood.repository.storage.Donor

object Constants {

    const val MOVIES_ARRAY_DATA_TAG = "results"
    const val ROOT_FRAGMENT_TAG = "root fragment"
    val DONOR_LIST_CLASS_TYPE = (ArrayList<Donor>()).javaClass
    const val THEATRE_BLOOD_BASE_URL = "https://api.themoviedb.org/3/discover/"
    const val IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/"
    const val SMALL_IMAGE_URL_PREFIX = IMAGE_URL_PREFIX + "w300"
    const val API_KEY_REQUEST_PARAM = "api_key"
    const val LANGUAGE_REQUEST_PARAM = "language"
    const val PAGE_REQUEST_PARAM = "page"
    const val API_KEY = "17c5889b399d3c051099e4098ad83493"
    const val LANGUAGE = "en"
    const val MAIN_DATABASE_NAME = "TheatreBlood.db"
    const val MODIFIED_DATABASE_NAME = "TheatreBloodModified.db"
    const val STANDARD_LEFT_AND_RIGHT_MARGIN = 20f
    const val STANDARD_EDIT_TEXT_SMALL_MARGIN = 10f
    const val STANDARD_EDIT_TEXT_HEIGHT = 60f
    const val STANDARD_GRID_EDIT_TEXT_HEIGHT = 60f
    const val STANDARD_BUTTON_HEIGHT = 50f
    const val STANDARD_GRID_HEIGHT = 120f
    const val EDIT_TEXT_TO_BUTTON_RATIO = 3  // 3:1
    const val REQUEST_CODE_ASK_PERMISSIONS = 123

    // toolbar

    const val DONATE_PRODUCTS_TITLE = "Donate Products"
    const val CREATE_PRODUCTS_TITLE = "Create Products"
    const val MANAGE_DONOR_TITLE = "Manage Donor"
    const val REASSOCIATE_DONATION_TITLE = "Reassociate Donation"
    const val UPDATE_TEST_RESULTS_TITLE = "Update Test Results"
    const val VIEW_DONOR_LIST_TITLE = "View Donor List"

}