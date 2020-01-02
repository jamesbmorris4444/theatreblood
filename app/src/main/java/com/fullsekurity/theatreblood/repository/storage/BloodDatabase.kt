package com.fullsekurity.theatreblood.repository.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.utils.Constants


@Database(entities = [Donor::class, Product::class], version = 1)
abstract class BloodDatabase : RoomDatabase() {

    abstract fun databaseDao(): DBDao

    companion object {
        private var mainInstance: BloodDatabase? = null
        private var stagingInstance: BloodDatabase? = null

        fun newInstance(context: Context, databaseName: String): BloodDatabase? {
            if (databaseName == Constants.MAIN_DATABASE_NAME) {
                if (mainInstance == null) {
                    mainInstance = Room.databaseBuilder(context, BloodDatabase::class.java, databaseName)
                        .allowMainThreadQueries()
                        .build()
                    LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX NEW >>>>>>>>>>>>>>>>>  %s", mainInstance))
                    return mainInstance
                } else {
                    return mainInstance
                }
            } else {
                if (stagingInstance == null) {
                    stagingInstance = Room.databaseBuilder(context, BloodDatabase::class.java, databaseName)
                        .allowMainThreadQueries()
                        .build()
                    LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX NEW >>>>>>>>>>>>>>>>>  %s", stagingInstance))
                    return stagingInstance
                } else {
                    return stagingInstance
                }
            }
        }

    }

}