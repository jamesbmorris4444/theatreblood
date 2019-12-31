package com.fullsekurity.theatreblood.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.fullsekurity.theatreblood.repository.storage.Donor

class Utils {

    companion object {
        fun hideKeyboard(view: View?) {
            if (view == null) return
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun donorEquals(donor: Donor, otherDonor: Donor): Boolean {
            return donor.title == otherDonor.title && donor.posterPath == otherDonor.posterPath && donor.voteCount == otherDonor.voteCount && donor.releaseDate == otherDonor.releaseDate
        }

        fun donorUnionStringForDistinctBy(donor: Donor): String {
            return donor.title + "," + donor.posterPath + "," + donor.voteCount.toString() + "," + donor.releaseDate
        }

    }

}