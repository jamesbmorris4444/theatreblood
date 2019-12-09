package com.fullsekurity.theatreblood.donors

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.repository.storage.datamodel.Donor
import com.fullsekurity.theatreblood.utils.ContextInjectorModule
import com.fullsekurity.theatreblood.utils.DaggerContextDependencyInjector
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class DonorsItemViewModel(val activity: Application) : RecyclerViewItemViewModel<Donor>() {

    internal var repository: Repository? = null
        @Inject set

    private val _voteCount = MutableLiveData<Int>().apply { value = 0 } ; val voteCount: LiveData<Int> = _voteCount
    private val _video = MutableLiveData<String>().apply { value = "" } ; val video: LiveData<String> = _video
    private val _voteAverage = MutableLiveData<Float>().apply { value = 0f } ; val voteAverage: LiveData<Float> = _voteAverage
    private val _title = MutableLiveData<String>().apply { value = "" } ; val title: LiveData<String> = _title
    private val _popularity = MutableLiveData<Float>().apply { value = 0f } ; val popularity: LiveData<Float> = _popularity
    private val _posterPath = MutableLiveData<String>().apply { value = "" } ; val posterPath: LiveData<String> = _posterPath
    private val _originalLanguage = MutableLiveData<String>().apply { value = "" } ; val originalLanguage: LiveData<String> = _originalLanguage
    private val _originalTitle = MutableLiveData<String>().apply { value = "" } ; val originalTitle: LiveData<String> = _originalTitle
    private val _backdropPath = MutableLiveData<String>().apply { value = "" } ; val backdropPath: LiveData<String> = _backdropPath
    private val _adult = MutableLiveData<String>().apply { value = "" } ; val adult: LiveData<String> = _adult
    private val _overview = MutableLiveData<String>().apply { value = "" } ; val overview: LiveData<String> = _overview
    private val _releaseDate = MutableLiveData<String>().apply { value = "" } ; val releaseDate: LiveData<String> = _releaseDate

    init {
        DaggerContextDependencyInjector.builder()
            .contextInjectorModule(ContextInjectorModule(activity.applicationContext))
            .build()
            .inject(this)
    }

    override fun setItem(donor: Donor) {
        val ( id, voteCount, video, voteAverage, title, popularity, posterPath, originalLanguage, originalTitle, backdropPath, adult, overview, releaseDate) = donor
        _voteCount.value = voteCount
        _video.value = if (video) "T" else "F"
        _voteAverage.value = voteAverage
        _title.value = title
        _popularity.value = popularity
        _posterPath.value = posterPath
        _originalLanguage.value = originalLanguage
        _originalTitle.value = originalTitle
        _backdropPath.value = backdropPath
        _adult.value = if (adult) "T" else "F"
        _overview.value = overview
        _releaseDate.value = releaseDate
    }

}
