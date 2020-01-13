package com.fullsekurity.theatreblood.repository.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Donor::class, Product::class], version = 1, exportSchema = false)
abstract class BloodDatabase : RoomDatabase() {

    abstract fun databaseDao(): DBDao

    companion object {

        @Volatile
        private var MAIN_INSTANCE: BloodDatabase? = null

        @Volatile
        private var STAGING_INSTANCE: BloodDatabase? = null

        fun newInstance(context: Context, mainDatabaseName: String, stagingDatabaseName: String): List<BloodDatabase> {
            synchronized(this) {
                val dbList: MutableList<BloodDatabase> = mutableListOf()
                val mainInstance = MAIN_INSTANCE
                val stagingInstance = STAGING_INSTANCE
                if (mainInstance == null) {
                    val mainBuilder = Room.databaseBuilder(context, BloodDatabase::class.java, mainDatabaseName).allowMainThreadQueries()
                    MAIN_INSTANCE = mainBuilder.build()
                    MAIN_INSTANCE?.let {
                        dbList.add(it)
                    }
                } else {
                    dbList.add(mainInstance)
                }
                if (stagingInstance == null) {
                    val stagingBuilder = Room.databaseBuilder(context, BloodDatabase::class.java, stagingDatabaseName).allowMainThreadQueries()
                    STAGING_INSTANCE = stagingBuilder.build()
                    STAGING_INSTANCE?.let {
                        dbList.add(it)
                    }
                } else {
                    dbList.add(stagingInstance)
                }
                return dbList
            }
        }
    }
}