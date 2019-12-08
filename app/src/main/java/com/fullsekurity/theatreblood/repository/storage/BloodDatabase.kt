package com.fullsekurity.theatreblood.repository.storage

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.repository.storage.datamodel.Donor
import com.fullsekurity.theatreblood.repository.storage.paging.DBDataSourceFactory
import com.fullsekurity.theatreblood.utils.Constants.DATA_BASE_NAME
import com.fullsekurity.theatreblood.utils.Constants.NUMBER_OF_THREADS
import java.util.concurrent.Executors

@Database(entities = [Donor::class], version = 1)
abstract class BloodDatabase : RoomDatabase() {

    var donors: LiveData<PagedList<Donor>>? = null

    abstract fun donorDao(): DBDao

    private fun init() {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(Integer.MAX_VALUE)
            .setPageSize(Integer.MAX_VALUE)
            .build()
        val executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        val dataSourceFactory = DBDataSourceFactory(donorDao())
        val livePagedListBuilder = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
        donors = livePagedListBuilder.setFetchExecutor(executor).build()
    }

    companion object {
        private var instance: BloodDatabase? = null
        private val sLock = Any()
        fun newInstance(context: Context): BloodDatabase? {
            synchronized(sLock) {
                LogUtils.W("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("INSTANCE--------->: %s", instance))
                if (instance == null) {
                    instance = Room.databaseBuilder(context, BloodDatabase::class.java, DATA_BASE_NAME).build()
                    instance?.let { it.init() }
                }
                return instance
            }
        }
    }
}