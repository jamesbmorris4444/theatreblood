package com.fullsekurity.theatreblood.donors

import androidx.databinding.ObservableField
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.theatreblood.repository.storage.Donor

@Suppress("UNCHECKED_CAST")
class DonorsItemViewModel(val activity: MainActivity) : RecyclerViewItemViewModel<Donor>() {

    private lateinit var donor: Donor

    val voteCount: ObservableField<String> = ObservableField("")
    val video: ObservableField<String> = ObservableField("")
    val voteAverage: ObservableField<String> = ObservableField("")
    val title: ObservableField<String> = ObservableField("")
    val popularity: ObservableField<String> = ObservableField("")
    val posterPath: ObservableField<String> = ObservableField("")
    val originalLanguage: ObservableField<String> = ObservableField("")
    val originalTitle: ObservableField<String> = ObservableField("")
    val backdropPath: ObservableField<String> = ObservableField("")
    val adult: ObservableField<String> = ObservableField("")
    val overview: ObservableField<String> = ObservableField("")
    val releaseDate: ObservableField<String> = ObservableField("")

    override fun setItem(item: Donor) {
        donor = item
        val ( id, _voteCount, _video, _voteAverage, _title, _popularity, _posterPath, _originalLanguage, _originalTitle, _backdropPath, _adult, _overview, _releaseDate) = item
        voteCount.set(_voteCount.toString())
        video.set(if (_video) "T" else "F")
        voteAverage.set(_voteAverage.toString())
        title.set(_title)
        popularity.set(_popularity.toString())
        posterPath.set(_posterPath)
        originalLanguage.set(_originalLanguage)
        originalTitle.set(_originalTitle)
        backdropPath.set(_backdropPath)
        adult.set(if (_adult) "T" else "F")
        overview.set(_overview)
        releaseDate.set(_releaseDate)
    }

    fun onItemClicked() {
        activity.transitionToSingleDonorFragment(donor)
    }

}
