package com.fullsekurity.theatreblood.repository

import android.content.Context
import androidx.room.Room
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.repository.storage.BloodDatabase
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.utils.Constants.DATA_BASE_NAME
import java.io.File
import javax.inject.Singleton

@Singleton
class Repository {

    lateinit var bloodDatabase: BloodDatabase

    fun initializeDataBase(donors: List<Donor>) {
        LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX   INSERT   %d     db=%s", donors.size, bloodDatabase))
        for (entry in donors.indices) {
            insertIntoDatabase(donors[entry])
        }
    }

    fun setBloodDatabase(context: Context) {
        bloodDatabase = BloodDatabase.newInstance(context)
    }

    private fun insertIntoDatabase(donor: Donor) {
        bloodDatabase.donorDao().insertDonor(donor)
    }

    fun getAllDonors(): List<Donor> {
        return bloodDatabase.donorDao().donors
    }

    fun deleteAllDonors() {
        bloodDatabase.donorDao().deleteAllDonors()
    }

    fun closeDatabase() {
        LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX   CLOSE"))
        bloodDatabase.let { bloodDatabase ->
            LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX   CLOSEaaaaaa"))
            if (bloodDatabase.isOpen) {
                LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX   CLOSEbbbbb"))
                bloodDatabase.close()
            }
        }
    }

    fun reopenDatabase(context: Context) {
        Room.databaseBuilder(context.applicationContext, BloodDatabase::class.java, DATA_BASE_NAME)
    }

    fun donorsFromFullName(search: String): List<Donor> {
        val list = getAllDonors()
        LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX  SEARCH     %d", list.size))
        var searchLast: String
        var searchFirst = "%"
        val index = search.indexOf(',')
        if (index < 0) {
            searchLast = "%$search%"
        } else {
            val last = search.substring(0, index)
            val first = search.substring(index + 1)
            searchFirst = "%$first%"
            searchLast = "%$last%"
        }
        LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX   %s   %s", searchLast, searchFirst)
        )
        var retval: List<Donor> = arrayListOf()
        bloodDatabase.donorDao()?.donorsFromFullName(searchLast, searchFirst)?.let {
            retval = it
        }
        return retval
    }

    fun deleteDatabase(context: Context) {
        LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX   DELETE"))
        context.deleteDatabase(DATA_BASE_NAME)
    }

    fun saveDatabase(context: Context) {
        val db = context.getDatabasePath(DATA_BASE_NAME)
        val dbShm = File(db.parent, DATA_BASE_NAME+"_shm")
        val dbWal = File(db.parent, DATA_BASE_NAME+"_wal")
        val dbBackup = File(db.parent, DATA_BASE_NAME+"_backup")
        val dbShmBackup = File(db.parent, DATA_BASE_NAME+"_shm_backup")
        val dbWalBackup = File(db.parent, DATA_BASE_NAME+"_wal_backup")
        if (db.exists()) {
            LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX   COPY 1"))
            db.copyTo(dbBackup, true)
        }
        if (dbShm.exists()) {
            LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX   COPY 2"))
            dbShm.copyTo(dbShmBackup, true)
        }
        if (dbWal.exists()) {
            LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("JIMX   COPY 3"))
            dbWal.copyTo(dbWalBackup, true)
        }
    }

}