package com.fullsekurity.theatreblood.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
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


class Repository(val activity: MainActivity) {

    private val TAG = Repository::class.java.simpleName
    private lateinit var bloodDatabase: BloodDatabase
    private val donorsService: APIInterface = APIClient.client
    private var disposable: Disposable? = null
    private var transportType = TransportType.NONE
    private var isMetered: Boolean = false
    private var cellularNetwork: Network? = null
    private var wiFiNetwork: Network? = null

    private enum class TransportType {
        NONE,
        CELLULAR,
        WIFI,
        BOTH
    }

    init {
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    setConnectedTransportType(connectivityManager, network)
                    isMetered = connectivityManager.isActiveNetworkMetered
                    LogUtils.W(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.NET), String.format("Network is connected, TYPE: %s (metered=%b)", transportType.name, isMetered))
                }

                override fun onLost(network: Network) {
                    setDisconnectedTransportType()
                    isMetered = false
                    LogUtils.W(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.NET), String.format("Network connectivity is lost, TYPE: %s (metered=%b)", transportType.name, isMetered))
                }
            }
        )
    }

    private fun setConnectedTransportType(connectivityManager: ConnectivityManager, network: Network) {
        when (transportType) {
            TransportType.NONE -> {
                connectivityManager.getNetworkCapabilities(network)?.let { networkCapabiities ->
                    if (networkCapabiities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        wiFiNetwork = network
                        transportType = TransportType.WIFI
                        activity.setToolbarNetworkStatus()
                    } else if (networkCapabiities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        cellularNetwork = network
                        transportType = TransportType.CELLULAR
                        activity.setToolbarNetworkStatus()
                    }
                }
            }
            TransportType.WIFI -> {
                connectivityManager.getNetworkCapabilities(network)?.let { networkCapabiities ->
                    if (networkCapabiities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        cellularNetwork = network
                        transportType = TransportType.BOTH
                        activity.setToolbarNetworkStatus()
                    }
                }
            }
            TransportType.CELLULAR -> {
                connectivityManager.getNetworkCapabilities(network)?.let { networkCapabiities ->
                    if (networkCapabiities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        wiFiNetwork = network
                        transportType = TransportType.BOTH
                        activity.setToolbarNetworkStatus()
                    }
                }

            }
            TransportType.BOTH -> { }
        }
    }

    private fun setDisconnectedTransportType() {
        when (transportType) {
            TransportType.NONE -> { }
            TransportType.WIFI, TransportType.CELLULAR -> {
                transportType = TransportType.NONE
                activity.setToolbarNetworkStatus()
            }
            TransportType.BOTH -> {
                val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                connectivityManager.getNetworkCapabilities(wiFiNetwork)?.let { networkCapabiities ->
                    if (networkCapabiities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        transportType = TransportType.WIFI
                        activity.setToolbarNetworkStatus()
                    }
                }
                if (transportType == TransportType.BOTH) {
                    transportType = TransportType.CELLULAR
                    activity.setToolbarNetworkStatus()
                }
                activity.setToolbarNetworkStatus()
            }
        }
    }

    fun getNetworkStatusResInt(): Int {
        when (transportType) {
            TransportType.NONE -> {
                return R.drawable.ic_network_status_none
            }
            TransportType.CELLULAR -> {
                return R.drawable.ic_network_status_cellular
            }
            TransportType.BOTH, TransportType.WIFI -> {
                return R.drawable.ic_network_status_wifi
            }
        }
    }

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