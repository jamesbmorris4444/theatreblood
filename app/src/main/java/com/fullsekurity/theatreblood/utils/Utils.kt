package com.fullsekurity.theatreblood.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class Utils {

    companion object {
        fun hideKeyboard(view: View?) {
            if (view == null) return
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}