package com.fullsekurity.theatreblood.repository

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.room.Room
import com.fullsekurity.theatreblood.R
import com.fullsekurity.theatreblood.activity.MainActivity
import com.fullsekurity.theatreblood.logger.LogUtils
import com.fullsekurity.theatreblood.modal.StandardModal
import com.fullsekurity.theatreblood.repository.network.APIClient
import com.fullsekurity.theatreblood.repository.network.APIInterface
import com.fullsekurity.theatreblood.repository.storage.BloodDatabase
import com.fullsekurity.theatreblood.repository.storage.Donor
import com.fullsekurity.theatreblood.utils.Constants
import com.fullsekurity.theatreblood.utils.Constants.DATA_BASE_NAME
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

class Repository {

    private val TAG = Repository::class.java.simpleName
    private lateinit var bloodDatabase: BloodDatabase
    private val donorsService: APIInterface = APIClient.client
    private var disposable: Disposable? = null

    fun initializeDatabase(progressBar: ProgressBar, activity: MainActivity) {
        disposable = donorsService.getDonors(Constants.API_KEY, Constants.LANGUAGE, 10)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe ({ donorResponse ->
                progressBar.visibility = View.GONE
                initializeDataBase(donorResponse.results)
            },
            {
                throwable -> iniitalizeDatabaseFailureModal(activity, throwable.message)
            })
    }

    private fun iniitalizeDatabaseFailureModal(activity: MainActivity, errorMessage: String?) {
        var error = errorMessage
        if (error == null) {
            error = "App cannot continue"
        }
        StandardModal(
            activity,
            modalType = StandardModal.ModalType.STANDARD,
            iconType = StandardModal.IconType.ERROR,
            titleText = activity.getString(R.string.std_modal_initialize_database_failure_title),
            bodyText = error,
            positiveText = activity.getString(R.string.std_modal_ok),
            dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                override fun onPositive(password: String) {
                    activity.finishActivity()
                }
                override fun onNegative() { }
                override fun onNeutral() { }
                override fun onBackPressed() {
                    activity.finishActivity()
                }
            }
        ).show(activity.supportFragmentManager, "MODAL")
    }

    fun onCleared() {
        disposable?.let {
            it.dispose()
            disposable = null
        }
    }

    fun initializeDataBase(donors: List<Donor>) {
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
        bloodDatabase.let { bloodDatabase ->
            if (bloodDatabase.isOpen) {
                bloodDatabase.close()
            }
        }
    }

    fun reopenDatabase(context: Context) {
        Room.databaseBuilder(context.applicationContext, BloodDatabase::class.java, DATA_BASE_NAME)
    }

    fun donorsFromFullName(search: String): List<Donor> {
        val list = getAllDonors()
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
        var retval: List<Donor> = arrayListOf()
        bloodDatabase.donorDao()?.donorsFromFullName(searchLast, searchFirst)?.let {
            retval = it
        }
        return retval
    }

    fun deleteDatabase(context: Context) {
        context.deleteDatabase(DATA_BASE_NAME)
    }

    fun saveDatabase(context: Context) {
        val db = context.getDatabasePath(DATA_BASE_NAME)
        val dbBackup = File(db.parent, DATA_BASE_NAME+"_backup")
        if (db.exists()) {
            db.copyTo(dbBackup, true)
            LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.ANX), String.format("Path Name \"%s\" exists and was backed up", db.toString()))
        }
    }

}