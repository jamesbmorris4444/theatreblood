package com.greendot.rewards.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.greendot.rewards.activity.MainActivity

class HomeViewModelFactory(private val activity: MainActivity) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(activity) as T
    }
}