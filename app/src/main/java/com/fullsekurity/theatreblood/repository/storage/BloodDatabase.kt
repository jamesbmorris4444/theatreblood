package com.fullsekurity.theatreblood.repository.storage

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fullsekurity.theatreblood.utils.Constants.DATA_BASE_NAME

@Database(entities = [Donor::class], version = 1)
abstract class BloodDatabase : RoomDatabase() {

    var donors: LiveData<List<Donor>>? = null

    abstract fun donorDao(): DBDao

    companion object {
        private lateinit var instance: BloodDatabase
        private val sLock = Any()
        fun newInstance(context: Context): BloodDatabase {
            synchronized(sLock) {
                instance = Room.databaseBuilder(context, BloodDatabase::class.java, DATA_BASE_NAME)
                    .allowMainThreadQueries()
                    .build()
                return instance
            }
        }
    }

}