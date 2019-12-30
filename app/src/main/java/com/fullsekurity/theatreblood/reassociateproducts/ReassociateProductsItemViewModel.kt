package com.fullsekurity.theatreblood.reassociateproducts

import androidx.databinding.ObservableField
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.theatreblood.repository.storage.Donor

@Suppress("UNCHECKED_CAST")
class ReassociateProductsItemViewModel(private val activityCallbacks: ActivityCallbacks) : RecyclerViewItemViewModel<Donor>() {

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
        voteCount.set(item.voteCount.toString())
        video.set(if (item.video) "T" else "F")
        voteAverage.set(item.voteAverage.toString())
        title.set(item.title)
        popularity.set(item.popularity.toString())
        posterPath.set(item.posterPath)
        originalLanguage.set(item.originalLanguage)
        originalTitle.set(item.originalTitle)
        backdropPath.set(item.backdropPath)
        adult.set(if (item.adult) "T" else "F")
        overview.set(item.overview)
        releaseDate.set(item.releaseDate)
    }

    fun onItemClicked() {
        activityCallbacks.fetchActivity().loadDonorFragment(donor)
    }

}
