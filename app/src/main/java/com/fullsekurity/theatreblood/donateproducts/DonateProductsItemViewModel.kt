package com.fullsekurity.theatreblood.donateproducts

import androidx.databinding.ObservableField
import com.fullsekurity.theatreblood.activity.ActivityCallbacks
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.theatreblood.repository.storage.Donor

@Suppress("UNCHECKED_CAST")
class DonateProductsItemViewModel(private val activityCallbacks: ActivityCallbacks) : RecyclerViewItemViewModel<Donor>() {

    private lateinit var donor: Donor

    val middleName: ObservableField<String> = ObservableField("")
    val video: ObservableField<String> = ObservableField("")
    val voteAverage: ObservableField<String> = ObservableField("")
    val lastName: ObservableField<String> = ObservableField("")
    val popularity: ObservableField<String> = ObservableField("")
    val firstName: ObservableField<String> = ObservableField("")
    val originalLanguage: ObservableField<String> = ObservableField("")
    val originalTitle: ObservableField<String> = ObservableField("")
    val backdropPath: ObservableField<String> = ObservableField("")
    val adult: ObservableField<String> = ObservableField("")
    val overview: ObservableField<String> = ObservableField("")
    val releaseDate: ObservableField<String> = ObservableField("")
    var inReassociate = false

    override fun setItem(item: Donor) {
        donor = item
        middleName.set(item.middleName.toString())
        video.set(if (item.video) "T" else "F")
        voteAverage.set(item.voteAverage.toString())
        lastName.set(item.lastName)
        popularity.set(item.popularity.toString())
        firstName.set(item.firstName)
        originalLanguage.set(item.originalLanguage)
        originalTitle.set(item.originalTitle)
        backdropPath.set(item.backdropPath)
        adult.set(if (item.adult) "T" else "F")
        overview.set(item.overview)
        releaseDate.set(item.releaseDate)
        this.inReassociate = item.inReassociate
    }

    fun onItemClicked() {
        if (inReassociate) {
            activityCallbacks.fetchActivity().reassociateIncorrectDonorClicked(donor)
        } else {
            activityCallbacks.fetchActivity().loadDonorFragment(donor, activityCallbacks.fetchActivity().transitionToCreateDonation)
        }
    }

}
