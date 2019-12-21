package com.fullsekurity.theatreblood.activity

import android.view.View
import android.widget.RadioButton

interface ActivityCallbacks {
    fun fetchActivity(): MainActivity
    fun fetchRootView() : View
    fun fetchRadioButton(resId: Int) : RadioButton?
}