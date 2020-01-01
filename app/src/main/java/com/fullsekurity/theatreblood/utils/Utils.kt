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

        fun newPatternOfSubpatterns(patternOfSubpatterns: String, index: Int, newPattern: String): String {
            // patternOfSubpatterns = P|P|P|...|P|
            // if there are N subpatterns then index = 0 to N-1
            val split: MutableList<String> = patternOfSubpatterns.split('|').toMutableList()
            val stringBuilder = StringBuilder()
            split[index] = newPattern
            for (newIndex in split.indices) {
                stringBuilder.append(split[newIndex])
                if (newIndex < split.size - 1) {
                    stringBuilder.append('|')
                }
            }
            return stringBuilder.toString()
        }

        fun getPatternOfSubpatterns(patternOfSubpatterns: String, index: Int): String {
            // patternOfSubpatterns = P|P|P|...|P|
            // if there are N subpatterns then index = 0 to N-1
            val split: MutableList<String> = patternOfSubpatterns.split('|').toMutableList()
            return split[index]
        }

    }

}