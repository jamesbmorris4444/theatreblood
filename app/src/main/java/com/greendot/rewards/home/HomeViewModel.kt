package com.greendot.rewards.home

import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import com.greendot.rewards.repository.Movie

class HomeViewModel : ViewModel() {
    private val homeDataModel = HomeDataModel()

    fun getTitle0(): String? {
        val movie: Movie? = homeDataModel?.let { it.getSingleMovie(0)}
        return movie?.let { it.title }
    }

    fun getReleaseDate0(): String? {
        val movie: Movie? = homeDataModel?.let { it.getSingleMovie(0)}
        return movie?.let { it.releaseDate }
    }

}