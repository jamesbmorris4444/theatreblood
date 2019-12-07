package com.fullsekurity.theatreblood.repository.storage.paging

import androidx.paging.PageKeyedDataSource
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.repository.storage.DBDao
import com.fullsekurity.theatreblood.repository.storage.datamodel.Movie

private val TAG = DBPageKeyedDataSource::class.java.simpleName
class DBPageKeyedDataSource(private val movieDao: DBDao) : PageKeyedDataSource<String, Movie>() {

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, Movie>) {
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("Loading Initial Range, Count %d", params.requestedLoadSize))
        val movies = movieDao.movies
        if (movies.isNotEmpty()) {
            callback.onResult(movies, "0", "1")
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Movie>) {}

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Movie>) {}

}