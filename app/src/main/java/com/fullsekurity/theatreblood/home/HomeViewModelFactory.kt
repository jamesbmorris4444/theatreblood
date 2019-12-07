package com.fullsekurity.theatreblood.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeViewModelFactory(private val activity: com.fullsekurity.theatreblood.activity.MainActivity) :
    ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(activity) as T
    }
}