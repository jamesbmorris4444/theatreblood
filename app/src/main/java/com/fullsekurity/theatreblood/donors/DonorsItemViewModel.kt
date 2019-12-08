package com.fullsekurity.theatreblood.donors

import android.app.Application
import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.theatreblood.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.theatreblood.repository.Repository
import com.fullsekurity.theatreblood.utils.ContextInjectorModule
import com.fullsekurity.theatreblood.utils.DaggerContextDependencyInjector
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class DonorsItemViewModelFactory(private val activity: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DonorsItemViewModel(activity) as T
    }
}

@Suppress("UNCHECKED_CAST")
class DonorsItemViewModel(val activity: Application) : RecyclerViewItemViewModel<DonorsDataModel>(activity) {

    private val context: Context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: Repository

    var voteCount: ObservableField<Int> = ObservableField(0)
    var video: ObservableField<String> = ObservableField("")
    var voteAverage: ObservableField<Float> = ObservableField(0f)
    var title: ObservableField<String> = ObservableField("")
    var popularity: ObservableField<Float> = ObservableField(0f)
    var posterPath: ObservableField<String> = ObservableField("")
    var originalLanguage: ObservableField<String> = ObservableField("")
    var originalTitle: ObservableField<String> = ObservableField("")
    var backdropPath: ObservableField<String> = ObservableField("")
    var adult: ObservableField<String> = ObservableField("")
    var overview: ObservableField<String> = ObservableField("")
    var releaseDate: ObservableField<String> = ObservableField("")

    init {
        DaggerContextDependencyInjector.builder()
            .contextInjectorModule(ContextInjectorModule(context))
            .build()
            .inject(this)
    }

    override fun setItem(donorsDataModel: DonorsDataModel) {
        val ( voteCount_, video_, voteAverage_, title_, popularity_, posterPath_, originalLanguage_, originalTitle_, backdropPath_, adult_, overview_, releaseDate_) = donorsDataModel.donorsDataObject
        voteCount.set(voteCount_)
        video.set(if (video_) "T" else "F")
        voteAverage.set(voteAverage_)
        title.set(title_)
        popularity.set(popularity_)
        posterPath.set(posterPath_)
        originalLanguage.set(originalLanguage_)
        originalTitle.set(originalTitle_)
        backdropPath.set(backdropPath_)
        adult.set(if (adult_) "T" else "F")
        overview.set(overview_ )
        releaseDate.set(releaseDate_)
    }

}
