package com.fullsekurity.theatreblood.repository.storage

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Donor::class, Product::class], version = 1)
abstract class BloodDatabase : RoomDatabase() {

    abstract fun databaseDao(): DBDao

    companion object {
        private lateinit var instance: BloodDatabase
        private val mIsDatabaseCreated = MutableLiveData<Boolean>()

        fun newInstance(context: Context, databaseName: String): BloodDatabase {
            instance = Room.databaseBuilder(context, BloodDatabase::class.java, databaseName)
                .allowMainThreadQueries()
                .build()
            updateDatabaseCreated(context, databaseName)
            return instance
        }

        private fun updateDatabaseCreated(context: Context, databaseName: String) {
            if (context.getDatabasePath(databaseName).exists()) {
                setDatabaseCreated()
            }
        }

        private fun setDatabaseCreated() {
            mIsDatabaseCreated.postValue(true)
        }

    }

}