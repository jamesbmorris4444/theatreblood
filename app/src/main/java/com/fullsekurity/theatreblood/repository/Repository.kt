package com.fullsekurity.theatreblood.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.repository.network.Network
import com.fullsekurity.theatreblood.repository.network.paging.NetworkDataSourceFactory
import com.fullsekurity.theatreblood.repository.storage.BloodDatabase
import com.fullsekurity.theatreblood.repository.storage.datamodel.Donor
import com.fullsekurity.theatreblood.repository.storage.datamodel.NetworkState
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Repository @Inject constructor(val context: Context) {
    private val TAG = Repository::class.java.simpleName
    private var disposable: Disposable? = null
    private val network: Network
    private var bloodDatabase: BloodDatabase? = null
    private val liveDataMerger: MediatorLiveData<PagedList<Donor>> = MediatorLiveData()

    private val boundaryCallback = object : PagedList.BoundaryCallback<Donor>() {
        override fun onZeroItemsLoaded() {
            super.onZeroItemsLoaded()
            LogUtils.W(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("boundaryCallback():"))
            liveDataMerger.addSource((bloodDatabase?.movies) as LiveData<PagedList<Donor>>) { value ->
                liveDataMerger.value = value
                liveDataMerger.removeSource((bloodDatabase?.movies) as LiveData<PagedList<Donor>>)
            }
        }
    }

    val movies: LiveData<PagedList<Donor>>
        get() = liveDataMerger

    val networkState: LiveData<NetworkState>
        get() = network.networkState

    init {
        val dataSourceFactory = NetworkDataSourceFactory()
        network = Network(dataSourceFactory, boundaryCallback)
        bloodDatabase = BloodDatabase.newInstance(context)
        liveDataMerger.addSource(network.pagedMovies) { value ->
            liveDataMerger.value = value
            LogUtils.W(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("constructor(1): %s", value.toString()))
        }

        if (bloodDatabase != null) {
            disposable = dataSourceFactory.getDonors()
                .observeOn(Schedulers.io())
                .subscribe{ donor -> insertIntoDatabase(donor) }
        }
    }

    private fun insertIntoDatabase(donor: Donor) {
        bloodDatabase?.movieDao()?.insertDonor(donor)
    }

    companion object {
        private var instance: Repository? = null
        fun getInstance(context: Context): Repository? {
            if (instance == null) {
                instance = Repository(context)
            }
            return instance
        }
    }
}