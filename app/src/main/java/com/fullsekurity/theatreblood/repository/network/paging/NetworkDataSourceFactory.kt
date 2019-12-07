package com.fullsekurity.theatreblood.repository.network.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.fullsekurity.theatreblood.repository.storage.datamodel.Movie
import io.reactivex.subjects.ReplaySubject



class NetworkDataSourceFactory internal constructor(): DataSource.Factory<String, Movie>() {
    val networkStatus: MutableLiveData<NetworkPageKeyedDataSource> = MutableLiveData()
    private val moviesPageKeyedDataSource: NetworkPageKeyedDataSource = NetworkPageKeyedDataSource()

    override fun create(): NetworkPageKeyedDataSource {
        networkStatus.postValue(moviesPageKeyedDataSource)
        return moviesPageKeyedDataSource
    }

    fun getMovies(): ReplaySubject<Movie> {
        return moviesPageKeyedDataSource.moviesObservable
    }

}