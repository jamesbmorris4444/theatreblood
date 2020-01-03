package com.fullsekurity.theatreblood.repository.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fullsekurity.theatreblood.utils.Constants


@Database(entities = [Donor::class, Product::class], version = 1)
abstract class BloodDatabase : RoomDatabase() {

    abstract fun databaseDao(): DBDao

    companion object {
        private var mainInstance: BloodDatabase? = null
        private var stagingInstance: BloodDatabase? = null
        private val sLockMain = Any()
        private val sLockStaging = Any()

        fun newInstance(context: Context, databaseName: String): BloodDatabase? {
            if (databaseName == Constants.MAIN_DATABASE_NAME) {
                synchronized(sLockMain) {
                    if (mainInstance == null) {
                        mainInstance =
                            Room.databaseBuilder(context, BloodDatabase::class.java, databaseName)
                                .allowMainThreadQueries()
                                .build()
                        return mainInstance
                    } else {
                        return mainInstance
                    }
                }
            } else {
                synchronized(sLockStaging) {
                    if (stagingInstance == null) {
                        stagingInstance =
                            Room.databaseBuilder(context, BloodDatabase::class.java, databaseName)
                                .allowMainThreadQueries()
                                .build()
                        return stagingInstance
                    } else {
                        return stagingInstance
                    }
                }
            }
        }
    }

}