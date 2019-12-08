package com.fullsekurity.theatreblood.recyclerview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel


abstract class RecyclerViewItemViewModel<T>(activity: Application) : AndroidViewModel(activity) {

    abstract fun setItem(item: T)

}