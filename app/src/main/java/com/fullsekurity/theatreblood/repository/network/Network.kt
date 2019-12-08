package com.fullsekurity.theatreblood.repository.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.repository.network.paging.NetworkDataSourceFactory
import com.fullsekurity.theatreblood.repository.storage.datamodel.Donor
import com.fullsekurity.theatreblood.repository.storage.datamodel.NetworkState
import com.fullsekurity.theatreblood.utils.Constants.LOADING_PAGE_SIZE
import com.fullsekurity.theatreblood.utils.Constants.NUMBER_OF_THREADS
import java.util.concurrent.Executors

class Network(
    dataSourceFactory: NetworkDataSourceFactory,
    boundaryCallback: PagedList.BoundaryCallback<Donor>
) {
    val pagedMovies: LiveData<PagedList<Donor>>
    var networkState: LiveData<NetworkState>

    init {
        LogUtils.D("MoviewNetwork", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("MoviewNetwork 1"))
        val pagedListConfig = PagedList.Config.Builder().setEnablePlaceholders(false)
            .setInitialLoadSizeHint(LOADING_PAGE_SIZE)
            .setPageSize(LOADING_PAGE_SIZE)
            .build()
        LogUtils.D("MoviewNetwork", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("MoviewNetwork 2"))
        networkState = Transformations.switchMap(dataSourceFactory.networkStatus) { source -> source.networkState }
        LogUtils.D("MoviewNetwork", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("MoviewNetwork 3"))
        val executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        LogUtils.D("MoviewNetwork", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("MoviewNetwork 4"))
        val livePagedListBuilder = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
        LogUtils.D("MoviewNetwork", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("MoviewNetwork 5"))
        pagedMovies = livePagedListBuilder.setFetchExecutor(executor).setBoundaryCallback(boundaryCallback).build()
        LogUtils.D("MoviewNetwork", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("MoviewNetwork 6"))

    }

}