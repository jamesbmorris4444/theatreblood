package com.fullsekurity.theatreblood.repository.storage.paging

import androidx.paging.DataSource
import com.fullsekurity.theatreblood.repository.storage.DBDao
import com.fullsekurity.theatreblood.repository.storage.datamodel.Movie

class DBDataSourceFactory internal constructor(dao: DBDao) : DataSource.Factory<String, Movie>() {
    private val moviesPageKeyedDataSource: DBPageKeyedDataSource = DBPageKeyedDataSource(dao)

    override fun create(): DBPageKeyedDataSource {
        return moviesPageKeyedDataSource
    }

}