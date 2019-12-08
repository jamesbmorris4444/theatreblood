package com.fullsekurity.theatreblood.repository.storage.paging

import androidx.paging.DataSource
import com.fullsekurity.theatreblood.repository.storage.DBDao
import com.fullsekurity.theatreblood.repository.storage.datamodel.Donor

class DBDataSourceFactory internal constructor(dao: DBDao) : DataSource.Factory<String, Donor>() {
    private val moviesPageKeyedDataSource: DBPageKeyedDataSource = DBPageKeyedDataSource(dao)

    override fun create(): DBPageKeyedDataSource {
        return moviesPageKeyedDataSource
    }

}